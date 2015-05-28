package service;

import domain.Challenge;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;

/**
 * Service class used to manage users
 *
 * @author Astrid
 */
@Stateless
@RequestScoped
public class ChallengeService extends GenericService<Challenge> {

    public ChallengeService() {
        super(Challenge.class);
    }
}
