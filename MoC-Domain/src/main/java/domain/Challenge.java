package domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * The Challenge class represents a coding challenge that can be given during a
 * Masters of Code competition. A challenge contains a name, difficulty,
 * location of the content of the challenge, and a set of hints that can be
 * given out during the challenge.
 *
 */
@Entity
public class Challenge implements Serializable{

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private String name;
    private int difficulty;

    private List<Hint> hints;
    // </editor-fold>
    @Id 
    @GeneratedValue
    private Long id;

    // <editor-fold defaultstate="collapsed" desc="constructor" >
    protected Challenge() {
        this.name = "Not set yet";
    }
    
    public Challenge(String name) {
        this.name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters" >
    /**
     * Function returns the name of the challenge.
     *
     * @return name of the challenge
     */
    public String getName() {
        return name;
    }

    /**
     * Function returns the difficulty of the challenge.
     *
     * @return difficulty of the challenge
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Function sets the difficulty of the challenge.
     *
     * @param difficulty
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    
    public boolean addHint(Hint h){
        if(!hints.contains(h))
        {
            hints.add(h);
            hintsChanged();
            return true;
        }
        return false;
    }
    
    /**
     * Gets a list of hints
     *
     * @return List of hints
     */
    public List<Hint> getHints() {
        return hints;
    }
    
    private void hintsChanged(){
        Collections.sort(hints, new HintComparator());
    }
    //</editor-fold>

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
