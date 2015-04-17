package domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.ejb.Singleton;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 * The Competition class represents a Masters of Code competition. A competition
 * contains a name, competitionDate, startTime and location. It also has a
 * minimum and maximum team size, a list of rounds and a list of participating
 * teams.
 *
 * @author TeamKoekje
 */
@Entity
@Singleton
public class Competition implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="variables" >
    @Id
    @GeneratedValue
    private long id;

    private String name;
    /*@Temporal(javax.persistence.TemporalType.DATE)
     private Date competitionDate;
     @Temporal(javax.persistence.TemporalType.DATE)
     private Date startTime;*/
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar startTime;
    private String location;

    private int minTeamSize;
    private int maxTeamSize;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Round> rounds;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Team> teams;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Round currentRound;

    @Transient
    private final Runnable timerRunnable;

    private final NewsFeed newsFeed;
    //</editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Constructor (singleton)" >
    private static Competition instance;

    protected Competition() {
        newsFeed = new NewsFeed();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (Calendar.getInstance().after(startTime) && currentRound != null) {
                    currentRound.secondExpired();
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(timerRunnable, 0, 1, TimeUnit.SECONDS);
    }

    public static Competition getInstance() {
        if (instance == null) {
            instance = new Competition();
        }
        return instance;
    }
    //</editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters" >
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar time) {
        this.startTime = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMinTeamSize() {
        return minTeamSize;
    }

    public void setMinTeamSize(int minTeamSize) {
        this.minTeamSize = minTeamSize;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Round getCurrentRound() {
        return currentRound;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Function adds a challenge to the competition.
     *
     * @param challenge challenge that is added to the competition
     * @param time time in seconds allowed for completing the challenge
     */
    public void addChallenge(Challenge challenge, int time) {
        //unneeded? SHOULD be add round, but we already create/remove/edit rounds using RoundService/DAO
    }

    /**
     * Function removes a challenge from the competition.
     *
     * @param challenge challenge that has to be removed
     */
    public void removeChallenge(Challenge challenge) {
        //unneeded? SHOULD be remove round, but we already create/remove/edit rounds using RoundService/DAO
    }

    /**
     * Function changes the position of a challenge within the competition.
     *
     * @param challenge challenge that has to change position
     * @param position the position to which the challenge should be set
     */
    public void changeChallengeOrder(Challenge challenge, int position) {
        //unneeded? SHOULD be edit round, but we already create/remove/edit rounds using RoundService/DAO
    }

    /**
     * Function add a participating team to the competition.
     *
     * @param team team that needs to be added
     */
    public void addTeam(Team team) {
        //unneeded? we already create/remove/edit rounds using TeamService/DAO
    }

    /**
     * Remove a participating team from the competition.
     *
     * @param team team that needs to be removed
     */
    public void removeTeam(Team team) {
        //unneeded? we already create/remove/edit rounds using TeamService/DAO

    }

    /**
     * Tells the current round the specified Team is done.
     *
     * @param toSubmit The Team that is done.
     */
    public void submit(Team toSubmit) {
        currentRound.submit(toSubmit);
    }

    public boolean joinTeam(String email, String token, long teamId) {
        for (Team t : teams) {
            if (t.getId() == teamId) {
                //t.addParticipant(user);
            }
        }
        return false;
    }
    //</editor-fold>
}
