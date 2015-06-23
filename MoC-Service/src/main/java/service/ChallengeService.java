package service;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import domain.Challenge;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
//</editor-fold>

/**
 * Service class used to manage users
 *
 * @author TeamKoekje
 */
@Stateless
@RequestScoped
public class ChallengeService extends GenericService<Challenge> {

    public ChallengeService() {
        super(Challenge.class);
    }
}
