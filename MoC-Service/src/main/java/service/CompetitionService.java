package service;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import com.sun.media.jfxmedia.logging.Logger;
import domain.Competition;
import domain.events.CompetitionEndedEvent;
import domain.events.CompetitionEvent;
import domain.events.HintReleasedEvent;
import domain.events.MessageReleasedEvent;
import domain.events.RoundEndedEvent;
import domain.events.UpdateEvent;
import domain.enums.CompetitionState;
import domain.enums.EventType;
import domain.events.RoundStartedEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.Query;
import websocket.WebsocketEndpoint;
// </editor-fold>

/**
 * An extension of the GenericService, used to keep track of active and future
 * competitions. Also handles the updates of the active competitions.
 *
 * @author TeamKoekje.
 */
@Singleton
@Startup
public class CompetitionService extends GenericService<Competition> {

    // <editor-fold defaultstate="collapsed" desc="variables" >
    private Timer timer;

    private List<Competition> activeCompetitions = new ArrayList<>();
    private List<Competition> futureCompetitions = new ArrayList<>();

    @Inject
    @Any
    Event<CompetitionEvent> competitionEvent;

    @Inject
    private WebsocketEndpoint we;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    /**
     * Initializes a new instance of the CompetitionService class.
     */
    public CompetitionService() {
        super(Competition.class);
    }

    @PostConstruct
    private void init() {
        Logger.logMsg(Logger.INFO, "Init of competitionService");
        timer = new Timer();
        timer.scheduleAtFixedRate(new CompetitionUpdateTask(), 1000, 1000);
    }

    private class CompetitionUpdateTask extends TimerTask {

        @Override
        public void run() {
            competitionEvent.fire(new UpdateEvent());
        }
    }

    /**
     * Loads the active and future competitions from the database.
     */
    public void loadComps() {
        Logger.logMsg(Logger.INFO, "Loading competitions...");
        List<Competition> competitions = findAll();
        Logger.logMsg(Logger.INFO, "Total competitions: " + competitions.size());
        Query q = em.createNamedQuery("Competition.findActive");
        q.setParameter("state", CompetitionState.ONGOING);
        activeCompetitions = (List<Competition>) q.getResultList();
        Logger.logMsg(Logger.INFO, "Active competitions: " + activeCompetitions.size());

        futureCompetitions = new ArrayList();
        for (Competition c : competitions) {
            if (c.getCompetitionState() == CompetitionState.NOT_STARTED) {
                futureCompetitions.add(c);
            }
        }
        Logger.logMsg(Logger.INFO, "Not-started competitions: " + futureCompetitions.size());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters" >    
    /**
     * Gets a List of Competitions, which are currently active.
     *
     * @return A List of Competition objects.
     */
    public List<Competition> getActiveCompetitions() {
        return activeCompetitions;
    }

    /**
     * Gets a List of Competitions, which have not been started yet.
     *
     * @return A List of Competition objects.
     */
    public List<Competition> getFutureCompetitions() {
        return futureCompetitions;
    }

    /**
     * Gets an active competition, with the specified Id.
     *
     * @param competitionId The Id of the competition to get.
     * @return The found competition, or null if the competition was not found.
     */
    public Competition getActiveCompetition(long competitionId) {
        for (Competition c : activeCompetitions) {
            if (c.getId() == competitionId) {
                return c;
            }
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Callback for CompetitionEvents. Depending on the type of the event will
     * perform different functions.
     *
     * @param event The CompetitionEvent that was fired.
     */
    public void HandleEvent(@Observes CompetitionEvent event) {
        switch (event.getType()) {
            case UPDATE:
                List<Competition> competitionsToRemove = new ArrayList<>();
                for (Competition c : activeCompetitions) {
                    for (CompetitionEvent e : c.update()) {
                        if (e.getType() == EventType.COMPETITION_ENDED) {
                            CompetitionEndedEvent cee = (CompetitionEndedEvent) e;
                            competitionsToRemove.add(cee.getCompetition());
                        } else {
                            competitionEvent.fire(e);
                        }
                    }
                }
                activeCompetitions.removeAll(competitionsToRemove);
                break;
            case ROUND_ENDED:
                RoundEndedEvent ree = (RoundEndedEvent) event;
                this.edit(ree.getCompetition());
                we.broadCast("{\"type\":\"roundended\",\"data\":\"roundended\"}");
                break;
            case COMPETITION_ENDED:
                CompetitionEndedEvent cee = (CompetitionEndedEvent) event;
                //TODO: test or when competition ends this works, if it is retrieved from the databse it may have another memmory adddress, so remove wont remove it.
                boolean result = activeCompetitions.remove(cee.getCompetition());
                this.edit(cee.getCompetition());
                break;
            case HINT_RELEASED:
                HintReleasedEvent hre = (HintReleasedEvent) event;
                we.broadCast("{\"hint\":{\"text\":\"" + hre.getReleasedHint().getContent() + "\"}}");
                break;
            case MESSAGE_RELEASED:
                MessageReleasedEvent mre = (MessageReleasedEvent) event;
                we.broadCast("{\"message\":{\"text\":\"" + mre.getReleasedMessage().getContent() + "\"}}");
                break;
            case ROUND_STARTED:
                RoundStartedEvent rse = (RoundStartedEvent) event;
                we.broadCast("{\"type\":\"roundstarted\",\"data\":\"roundstarted\"");
                break;
            default:
                throw new AssertionError(event.getType().name());
        }
    }

    /**
     * Replaces a future competition with the specified competition, by matching
     * their Id. This should be used when a competition is retrieved from the
     * database, instead of here. TODO: override equals and hashcodes of domain
     * objects (such as competition), so they are simply identified by their Id
     * and more easily managed.
     *
     * @param c The new value of the competition.
     */
    public void replaceFutureCompetition(Competition c) {
        for (Competition fc : futureCompetitions) {
            if (c.getId() == fc.getId()) {
                futureCompetitions.remove(fc);
                futureCompetitions.add(c);
                return;
            }
        }
    }

    /**
     * Replaces an active competition with the specified competition, by
     * matching their Id. This should be used when a competition is retrieved
     * from the database, instead of here. TODO: override equals and hashcodes
     * of domain objects (such as competition), so they are simply identified by
     * their Id and more easily managed.
     *
     * @param c The new value of the competition.
     */
    public void replaceActiveCompetition(Competition c) {
        for (Competition ac : activeCompetitions) {
            if (c.getId() == ac.getId()) {
                activeCompetitions.remove(ac);
                activeCompetitions.add(c);
                return;
            }
        }
    }

    /**
     * Adds the specified Competition to the list of active competitions, and
     * removes it from the list of future competitions. This is mainly used when
     * a competition is started.
     *
     * @param c The competition to add.
     */
    public void addActiveCompetition(Competition c) {
        activeCompetitions.add(c);
        removeFutureCompetition(c);
    }

    /**
     * Adds the specified Competition to the list of future competitions. This
     * is mainly used when a new competition is created.
     *
     * @param c The Competition to add.
     */
    public void addFutureCompetition(Competition c) {
        futureCompetitions.add(c);
    }

    /**
     * Removes the specified Competition from the list of future competitions.
     * This is mainly used when a competition is started.
     *
     * @param c The competition to remove
     */
    public void removeFutureCompetition(Competition c) {
        for (Competition fc : futureCompetitions) {
            if (fc.getId() == c.getId()) {
                futureCompetitions.remove(fc);
                return;
            }
        }
    }
    // </editor-fold>
}
