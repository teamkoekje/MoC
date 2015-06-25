package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;

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
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int difficulty = 1;
    private long suggestedDuration;
    
    private String author = "Not filled in";
    private String organisation = "Not filled in";
    private String website = "";
    
    private String descriptionParticipant = "Not filled in";;
    private String descriptionSpectator = "Not filled in";

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    @XmlElement
    private List<Hint> hints;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor" >
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        if (difficulty < 1) {
            this.difficulty = 1;
        } else if (difficulty > 3) {
            this.difficulty = 3;
        } else {
            this.difficulty = difficulty;
        }
    }

    public long getSuggestedDuration() {
        return suggestedDuration;
    }

    public void setSuggestedDuration(long suggestedDuration) {
        this.suggestedDuration = suggestedDuration;
    }

    public List<Hint> getHintsCopy() {
        return new ArrayList<>(hints);
    }

    private void hintsChanged() {
        Collections.sort(hints, new HintComparator());
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }
    
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescriptionParticipant() {
        return descriptionParticipant;
    }

    public void setDescriptionParticipant(String descriptionParticipant) {
        this.descriptionParticipant = descriptionParticipant;
    }

    public String getDescriptionSpectator() {
        return descriptionSpectator;
    }

    public void setDescriptionSpectator(String descriptionSpectator) {
        this.descriptionSpectator = descriptionSpectator;
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
