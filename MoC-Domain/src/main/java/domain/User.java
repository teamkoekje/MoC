package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "MOC_USER")
public class User implements Serializable {

//    @Id
    @GeneratedValue
    @XmlAttribute
    private long id;

    @Id
    private String username;

    private String email;
    @XmlTransient
    private String password;
    private String name;
    private String organisation;

    @OneToMany(mappedBy = "initiator", cascade = CascadeType.ALL)
    private List<Team> teams;

    @ElementCollection
    protected final List<String> userGroups = new ArrayList<>();

    public User(long id) {
        this.id = id;
        this.userGroups.add(UserGroup.USER);
    }

    protected User() {
        this.userGroups.add(UserGroup.USER);
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void removeTeam(Team team) {
        this.teams.remove(team);
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
