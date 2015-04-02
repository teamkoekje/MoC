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

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private String name;
    private int difficulty;

    private List<Hint> hints;
    // </editor-fold>

    public Challenge(String name) {
        this.name = name;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters" >
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

    /**
     * Gets a list of hints
     * @param u the user requesting the hints, must be a manager
     * @return List of hints
     */
    public List<Hint> getHints(User u) {
        
        return hints;
    }
    //</editor-fold>
}
