package service;

import domain.Competition;
import domain.Events.CompetitionEndedEvent;
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
    private Timer timer;

    @Inject
    @Any
    Event<CompetitionEvent> competitionEvent;

    public CompetitionService() {
        super(Competition.class);
    }

    public void HandleEvent(@Observes CompetitionEvent event) {
        System.out.println("Hallo event: " + event.getType());
        switch (event.getType()) {
            case ROUND_ENDED:
                break;
            case COMPETITION_ENDED:
                CompetitionEndedEvent cee = (CompetitionEndedEvent) event;
                System.out.println("competition ended: " + cee.getCompetition().getName());
                competitions.remove(cee.getCompetition());
                break;
            case HINT_RELEASED:
                break;
            default:
                throw new AssertionError(event.getType().name());
        }
    }

    private class CompetitionUpdateTask extends TimerTask {
        @Override
        public void run() {
            for (Competition c : competitions) {
                System.out.println("updating competition" + c.getName());
                for (CompetitionEvent e : c.update()) {
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
        timer.scheduleAtFixedRate(new CompetitionUpdateTask(), 1000, 1000);
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
