/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Date;
import java.util.List;

/**
 * The Competition class represents a Masters of Code competition. A competition
 * contains a name, date, startTime and location. It also has a minimum and
 * maximum team size, a list of rounds and a list of participating teams.
 *
 * @author Astrid Belder
 */
public class Competition {

    private String name;
    private Date date;
    private Date startTime;
    private String location;

    private int minTeamSize;
    private int maxTeamSize;

    private List<Round> rounds;
    private List<Team> teams;

    public Competition() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters" >
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
    // </editor-fold>

    /**
     * Function adds a challenge to the competition.
     *
     * @param challenge challenge that is added to the competition
     * @param time time in seconds allowed for completing the challenge
     */
    public void addChallenge(Challenge challenge, int time) {

    }

    /**
     * Function removes a challenge from the competition.
     *
     * @param challenge challenge that has to be removed
     */
    public void removeChallenge(Challenge challenge) {

    }

    /**
     * Function changes the position of a challenge within the competition.
     *
     * @param challenge challenge that has to change position
     * @param position the position to which the challenge should be set
     */
    public void changeChallengeOrder(Challenge challenge, int position) {

    }

    /**
     * Function add a participating team to the competition.
     *
     * @param team team that needs to be added
     */
    public void addTeam(Team team) {

    }

    /**
     * Remove a participating team from the competition.
     *
     * @param team team that needs to be removed
     */
    public void removeTeam(Team team) {

    }

}
