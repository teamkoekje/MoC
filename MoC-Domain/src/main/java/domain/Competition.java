package domain;

import domain.Events.CompetitionEndedEvent;
import domain.Events.CompetitionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The Competition class represents a Masters of Code competition. A competition
 * contains a name, competitionDate, startTime and location. It also has a
 * minimum and maximum team size, a list of rounds and a list of participating
 * teams.
 *
 * @author TeamKoekje
 */
@Entity
public class Competition implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="variables" >
    @Id
    @GeneratedValue
    @XmlAttribute
    private long id;

    private String name;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date competitionDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endTime;

    private String location;

    private int minTeamSize;
    private int maxTeamSize;

    @OneToMany(cascade = CascadeType.PERSIST)
    private final List<Round> rounds = new ArrayList<>();
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private final List<Team> teams = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    private Round currentRound;

    private final NewsFeed newsFeed;
    //</editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
    public Competition() {
        newsFeed = new NewsFeed();
    }

    public Competition(String name, Date competitionDate, Date startingTime, Date endTime, String location) {
        this.name = name;
        this.competitionDate = competitionDate;
        this.startTime = startingTime;
        this.endTime = endTime;
        this.location = location;

        newsFeed = new NewsFeed();
    }
    //</editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters" >
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCompetitionDate() {
        return competitionDate;
    }

    public void setCompetitionDate(Date competitionDate) {
        this.competitionDate = competitionDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
    public List<CompetitionEvent> update() {
        if (Calendar.getInstance().after(startTime) && currentRound != null) {
            return currentRound.update();
        }
        List<CompetitionEvent> events = new ArrayList<>();
        events.add(new CompetitionEndedEvent(this));
        return events;
    }

    /**
     * Function adds a challenge to the competition.
     *
     * @param challenge challenge that is added to the competition
     * @param time time in seconds allowed for completing the challenge
     */
    public void addChallenge(Challenge challenge, int time) {
        rounds.add(new Round(challenge, time));
    }

    /**
     * Function removes a challenge from the competition.
     *
     * @param challenge challenge that has to be removed
     */
    public void removeChallenge(Challenge challenge) {
        for (Round r : rounds) {
            if (r.getChallenge() == challenge) {
                rounds.remove(r);
                return;
            }
        }
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
        teams.add(team);
    }

    /**
     * Remove a participating team from the competition.
     *
     * @param team team that needs to be removed
     */
    public void removeTeam(Team team) {
        teams.remove(team);

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

    public boolean participantIsInTeam(User p) {
        for (Team t : teams) {
            if (t.getParticipants().contains(p)) {
                return true;
            }
        }
        return false;
    }
    //</editor-fold>
}
