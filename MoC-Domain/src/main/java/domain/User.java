package domain;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
// </editor-fold>

@Entity
@Table(name = "MOC_USER")

@NamedQueries({
    @NamedQuery(name = "User.searchUsers",
            query = "SELECT user FROM User user WHERE user.name LIKE :searchInput OR user.username LIKE :searchInput OR user.email LIKE :searchInput")
})


public class User implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Variables" >
    @GeneratedValue
    @XmlAttribute
    private long id;

    @Id
    private String username;

    private String email;
    private String password;
    private String name;
    private String organisation;

    @ManyToMany(mappedBy = "participants", cascade = CascadeType.ALL)
    private final List<Team> teams = new ArrayList<>();

    @ElementCollection
    protected final List<String> userGroups = new ArrayList<>();

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructor(s)" >
    public User(long id) {
        this.id = id;
        this.userGroups.add(UserGroup.USER);
    }

    public User(String username, String email, String password, String name, String organisation) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.organisation = organisation;
        this.userGroups.add(UserGroup.USER);
    }

    protected User() {
        this.userGroups.add(UserGroup.USER);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter & Setters" >
    /**
     * Gets the list of Teams this User is in.
     *
     * @return The list of Teams this User is in.
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Gets the Id of this User.
     *
     * @return The Id of this User.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets this User's Email
     *
     * @return A String containing this User's Email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets this User's Email.
     *
     * @param email The new value for this User's Email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of this User.
     *
     * @return A String containing the password of this User.
     */
    //@XmlTransient
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of this User.
     *
     * @param password The new value for this User's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the Name of this User.
     *
     * @return A String containing the name of this User.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Name of this User.
     *
     * @param name The new value for this User's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the organisation of this User.
     *
     * @return A String containing the organisation of this User.
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * Sets the organisation for this User.
     *
     * @param organisation The new value for this User's organisation.
     */
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    /**
     * Gets the username of this User.
     *
     * @return A String containing the Username of this User.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of this User.
     *
     * @param username The new value for this User's username.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Adds a Team in which this User participates.
     *
     * @param team The Team to add.
     */
    public void addTeam(Team team) {
        this.teams.add(team);
    }

    /**
     * Remove a Team in which this User participates.
     *
     * @param team The Team to remove.
     */
    public void removeTeam(Team team) {
        this.teams.remove(team);
    }
    // </editor-fold>
}
