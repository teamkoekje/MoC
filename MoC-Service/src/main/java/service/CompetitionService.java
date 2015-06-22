package service;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import domain.Competition;
import domain.events.CompetitionEndedEvent;
import domain.events.CompetitionEvent;
import domain.events.HintReleasedEvent;
import domain.events.MessageReleasedEvent;
import domain.events.RoundEndedEvent;
import domain.events.UpdateEvent;
import domain.enums.CompetitionState;
import domain.enums.EventType;
import java.util.ArrayList;
import java.util.Date;
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
    public CompetitionService() {
        super(Competition.class);
    }

    @PostConstruct
    private void init() {
        System.out.println("Init of competitionService");
        timer = new Timer();
        timer.scheduleAtFixedRate(new CompetitionUpdateTask(), 1000, 1000);
    }

    private class CompetitionUpdateTask extends TimerTask {

        @Override
        public void run() {
            competitionEvent.fire(new UpdateEvent());
        }
    }

    public void loadComps() {
        System.out.println("Loading competitions...");
        List<Competition> competitions = findAll();
        System.out.println("Total competitions: " + competitions.size());
        Query q = em.createNamedQuery("Competition.findActive");
        q.setParameter("state", CompetitionState.ONGOING);
        activeCompetitions = (List<Competition>) q.getResultList();
        System.out.println("Active competitions: " + activeCompetitions.size());

        futureCompetitions = new ArrayList();
        for (Competition c : competitions) {
            if (c.getCompetitionState() == CompetitionState.NOT_STARTED) {
                futureCompetitions.add(c);
            }
        }
        System.out.println("Not-started competitions: " + futureCompetitions.size());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters" >    
    public List<Competition> getActiveCompetitions() {
        return activeCompetitions;
    }

    public List<Competition> getFutureCompetitions() {
        return futureCompetitions;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    public void HandleEvent(@Observes CompetitionEvent event) {
        switch (event.getType()) {
            case UPDATE:
                List<Competition> competitionsToRemove = new ArrayList<>();
                for (Competition c : activeCompetitions) {
                    for (CompetitionEvent e : c.update()) {
                        if (e.getType() == EventType.COMPETITION_ENDED) {
                            CompetitionEndedEvent cee = (CompetitionEndedEvent)e;
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
                break;
            case COMPETITION_ENDED:
                CompetitionEndedEvent cee = (CompetitionEndedEvent) event;
                //test or when competition ends this works, if it is retrieved from the databse it may have another memmory adddress, so remove wont remove it.
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
            default:
                throw new AssertionError(event.getType().name());
        }
    }

    public void replaceActiveCompetition(Competition c) {
        for (Competition ac : activeCompetitions) {
            if (c.getId() == ac.getId()) {
                activeCompetitions.remove(ac);
                activeCompetitions.add(c);
                return;
            }
        }
    }

    public void addActiveCompetition(Competition c) {
        activeCompetitions.add(c);
        futureCompetitions.remove(c);
    }

    public void addFutureCompetition(Competition c) {
        futureCompetitions.add(c);
    }

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
