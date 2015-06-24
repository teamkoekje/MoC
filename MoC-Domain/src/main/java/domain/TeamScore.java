/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Robin
 */
@Entity
public class TeamScore implements Serializable {
    
    @Id @GeneratedValue
    private long id;
    
    @XmlAttribute
    private String team;
    @XmlAttribute
    private long score;

    protected TeamScore() {
    
    }

    public TeamScore(String team, long score) {
        this.team = team;
        this.score = score;
    }

    public String getTeam() {
        return team;
    }

    public long getScore() {
        return score;
    }

    public long getId() {
        return id;
    }
    
    public void increaseScore(long score){
        this.score += score;
    }
}
