package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * The Challenge class represents a coding challenge that can be given during a
 * Masters of Code competition. A challenge contains a name, difficulty,
 * location of the content of the challenge, and a set of hints that can be
 * given out during the challenge.
 *
 * @author TeamKoekje
 */
@Entity
public class Challenge implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    private String name;
    private int difficulty;

    @OneToMany
    private List<Hint> hints;
    // </editor-fold>

    @Id
    @GeneratedValue
    private Long id;

    // <editor-fold defaultstate="collapsed" desc="constructor" >
    protected Challenge() {
        this.name = "Not set yet";
        this.hints = new ArrayList<>();
    }

    public Challenge(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can't be null");
        }
        this.name = name;
        this.hints = new ArrayList<>();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters" >
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Iterator<Hint> hintsIterator() {
        return hints.iterator();
    }

    private void hintsChanged() {
        Collections.sort(hints, new HintComparator());
    }
    //</editor-fold>

    public boolean addHint(Hint h) {
        if (!hints.contains(h)) {
            hints.add(h);
            hintsChanged();
            return true;
        }
        return false;
    }
}
