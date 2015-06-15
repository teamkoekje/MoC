package service;

import domain.Challenge;
import domain.Competition;
import domain.Team;
import domain.User;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * Service class used to manage users
 *
 * @author Astrid
 */
@Singleton
@Startup
public class TestDataService {

    @Inject
    UserService userService;

    @Inject
    CompetitionService competitionService;

    @Inject
    TeamService teamService;

    @Inject
    InvitationService invitationService;

    @Inject
    ChallengeService challengeService;

    @PostConstruct
    private void init() {
        System.out.println("Generating test data");

        User u1 = new User(1);
        u1.setUsername("Strike");
        u1.setName("Arno Schellekens");
        u1.setEmail("Arno@Arno.nl");
        u1.setPassword("qwerty");
        u1.setOrganisation("Fontys");

        User u2 = new User(2);
        u2.setUsername("Obsidian");
        u2.setName("Casper Linschooten");
        u2.setEmail("Casper@Pizzaplace.nl");
        u2.setPassword("qwerty");
        u2.setOrganisation("Fontys");

        userService.create(u1);
        userService.create(u2);

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        cal.add(Calendar.DATE, +2);
        Date tomorrow = cal.getTime();
        Competition c1 = new Competition("Competition1", tomorrow, new Date(), new Date(), "Fontys");
        Competition c2 = new Competition("Competition2", yesterday, new Date(), new Date(), "Fontys");
        Competition c3 = new Competition("Competition3", today, new Date(), new Date(), "Fontys");
        Competition c4 = new Competition("Competition4", tomorrow, new Date(), new Date(), "Fontys");

        Team t1 = new Team("Team1", c1);
        Team t2 = new Team("Team2", c1);
        Team t3 = new Team("Team3", c2);

        t1.setOwner(u1);
        t2.setOwner(u2);
        t3.setOwner(u1);

        t1.addParticipant(u2);
        t2.addParticipant(u1);

        teamService.create(t1);
        teamService.create(t2);
        teamService.create(t3);

        c1.addTeam(t1);
        c1.addTeam(t2);
        c2.addTeam(t3);

        Challenge ch1 = new Challenge("Challenge1");        
        Challenge ch2 = new Challenge("Challenge2");       
        Challenge ch3 = new Challenge("Challenge3");

        c1.addChallenge(ch1, 30);
        c1.addChallenge(ch2, 30);
        c1.addChallenge(ch3, 30);

        competitionService.create(c1);
        competitionService.create(c2);
        competitionService.create(c3);
        competitionService.create(c4);

        invitationService.inviteMember("casper@pizzaplace.nl", t3.getId());
        
        competitionService.loadComps();
        //invitationService.inviteMember("TeamKoekje@gmail.com", t1.getId());
    }
}
