package service;

import domain.Competition;
import domain.Events.CompetitionEndedEvent;
import domain.Events.CompetitionEvent;
import domain.Events.HintReleasedEvent;
import domain.Events.RoundEndedEvent;
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
                RoundEndedEvent ree = (RoundEndedEvent) event;
                //TODO: Edit in database
                break;
            case COMPETITION_ENDED:
                CompetitionEndedEvent cee = (CompetitionEndedEvent) event;
                competitions.remove(cee.getCompetition());
                //TODO: Edit in database
                break;
            case HINT_RELEASED:
                HintReleasedEvent hre = (HintReleasedEvent) event;
                //TODO: Send the released hint to the newsfeed
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

    public List<Competition> getActiveCompetitions() {
        List<Competition> activeCompetitions = new ArrayList();
        for (Competition c : competitions) {
            if (c.getCurrentRound() != null) {
                activeCompetitions.add(c);
            } else {
                //empty round. so its not an active competition
            }
        }
        return activeCompetitions;
    }

    public List<Competition> geFutureCompetitions() {
        List<Competition> futureCompetitions = new ArrayList();
        Date currentDate = new Date();
        currentDate.setTime(System.currentTimeMillis());
        
        
        for (Competition c : competitions) {
            if (c.getCompetitionDate().after(currentDate)) {
                futureCompetitions.add(c);
            } else {
                //date is before today. so its not an future competition
            }
        }
        return futureCompetitions;
    }

}
