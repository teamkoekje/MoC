package domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Admin extends User implements Serializable {

    @ManyToMany
    private final Set<Competition> competitions = new HashSet<>();

    public Admin(long id) {
        super(id);
        this.userGroups.add(UserGroup.ADMIN);
    }

    protected Admin() {
        super();
        this.userGroups.add(UserGroup.ADMIN);
    }
    
    public void addCompetition(Competition competition){
        this.competitions.add(competition);
    }
    
    public void removeCompetition(Competition competition){
        this.competitions.remove(competition);
    }
}
