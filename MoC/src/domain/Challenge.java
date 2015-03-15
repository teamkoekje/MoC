/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.List;

/**
 * The Challenge class represents a coding challenge that can be given during a
 * Masters of Code competition. A challenge contains a name, difficulty,
 * location of the content of the challenge, and a set of hints that can be
 * given out during the challenge.
 *
 * @author Astrid Belder
 */
public class Challenge {

    private String name;
    private int difficulty;

    private List<Hint> hints;

    public Challenge(String name) {
        this.name = name;
    }

    /**
     * Function returns the name of the challenge.
     * @return name of the challenge
     */
    public String getName() {
        return name;
    }

    /**
     * Function returns the difficulty of the challenge.
     * @return difficulty of the challenge
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Function sets the difficulty of the challenge.
     * @param difficulty 
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
