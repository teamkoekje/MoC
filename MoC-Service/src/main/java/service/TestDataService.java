package service;

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

    //@PostConstruct
    private void init() {
        System.out.println("Generating test data");

        User u1 = new User(1);
        u1.setUsername("Strike");
        u1.setEmail("Arno@Arno.nl");
        u1.setPassword("qwerty");
        u1.setOrganisation("Fontys");

        User u2 = new User(2);
        u2.setUsername("Obsidian");
        u2.setEmail("Casper@Pizzaplace.nl");
        u2.setPassword("qwerty");
        u2.setOrganisation("Fontys");

        userService.create(u1);
        userService.create(u2);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        cal.add(Calendar.DATE, +2);
        Date tomorrow = cal.getTime();
        Competition c1 = new Competition("Competition1", new Date(), new Date(), new Date(), "Fontys");
        Competition c2 = new Competition("Competition2", yesterday, new Date(), new Date(), "Fontys");
        Competition c3 = new Competition("Competition3", tomorrow, new Date(), new Date(), "Fontys");

        Team t1 = new Team(u1, "Team1");
        Team t2 = new Team(u2, "Team2");
        Team t3 = new Team(u1, "Team3");

        t1.setCompetition(c1);
        t2.setCompetition(c1);
        t3.setCompetition(c2);

        teamService.create(t1);
        teamService.create(t2);
        teamService.create(t3);

        c1.addTeam(t1);
        c1.addTeam(t2);
        c2.addTeam(t3);

        competitionService.create(c1);
        competitionService.create(c2);
        competitionService.create(c3);
    }
}
