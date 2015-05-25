package service;

import domain.Competition;
import domain.Events.CompetitionEvent;
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

@Singleton
@Startup
public class CompetitionService extends GenericService<Competition> {

    private List<Competition> competitions = new ArrayList<>();
    Timer timer;

    @Inject
    @Any
    Event<CompetitionEvent> competitionEvent;
    /*
     - 1 Timer update alle lopende competities.
     - Lopende competities worden bij het opstarten van de service opgehaald en in een lijstje gestopt
     */

    public CompetitionService() {
        super(Competition.class);
    }

    public void HandleEvent(@Observes CompetitionEvent event) {
        System.out.println("Hallo event: " + event.getType());
        switch (event.getType()) {
            case ROUND_ENDED:
                break;
            case COMPETITION_ENDED:
                //System.out.println("competitions found: " + this.findAll().size());
                break;
            case HINT_RELEASED:
                break;
            case NONE:
                break;
            default:
                throw new AssertionError(event.getType().name());

        }
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {

            for (Competition c : competitions) {
                System.out.println("updating competitie" + c.getName());
                for (CompetitionEvent e : c.update()) {
                    //System.out.println("Firing event" + e.getType());
                    competitionEvent.fire(e);
                }
            }
        }
    }

    @PostConstruct
    private void init() {
        System.out.println("Init of competitionService");
        competitions = findAll();
        timer = new Timer();
        //timer.scheduleAtFixedRate(new RemindTask(), 1000, 1000);
    }

    public boolean joinTeam(String email, String token, long competitionId, long teamId) {
        Competition c = this.findById(competitionId);
        boolean result = c.joinTeam(email, token, teamId);
        if (result) {
            this.edit(c);
        }
        return result;
    }

}
