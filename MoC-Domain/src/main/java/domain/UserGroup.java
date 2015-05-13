package domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserGroup implements Serializable{

    @Id
    private Long id;

    public static final String USER = "User";
    public static final String ADMIN = "Admin";
}
