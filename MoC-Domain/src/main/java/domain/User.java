package domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public abstract class User {

    @Id
    @GeneratedValue
    private long id;

    private String email;
    private String password;
    private String name;

    public User() {

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

}
