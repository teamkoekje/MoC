package service;

import dao.AbstractDAO;
import dao.InvititationDAO;
import dao.TeamDAO;
import domain.Invitation;
import domain.Team;
import domain.User;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

@Stateless
@RequestScoped
public class TeamService extends AbstractService<Team> {

    @Inject
    private TeamDAO teamDao;
    
    @Inject
    private InvititationDAO invitationDao;

    private final SecureRandom random = new SecureRandom();

    @Override
    protected AbstractDAO getDAO() {
        return teamDao;
    }

    /**
     * Find all teams that belong to a certain competition
     *
     * @param competitionId the id of the competition
     * @return list with teams that belong to the competition
     */
    public List<Team> findByCompetition(long competitionId) {
        return teamDao.findByCompetition(competitionId);
    }

    //<editor-fold defaultstate="collapsed" desc="Invites">
    /**
     * Invites a member to a certain team
     *
     * @param email email address of the person that should be invited
     * @param teamId id of the team that the person should be invited to
     */
    public void inviteMember(String email, long teamId) {
        //Generate token
        String token = generateToken();
        Team t = teamDao.findById(teamId);
        Invitation invite = new Invitation(t, email, token);
        invitationDao.create(invite);
        
        //send email
    }

    /**
     * Lets a user join a certain team
     *
     * @param user user that should join the team
     * @param token string to verify if the user is allowed to join the team
     * @param teamId id of the team that the user should join
     */
    public void joinTeam(User user, String token, long teamId) {
        Team t = teamDao.findById(teamId);

        throw new UnsupportedOperationException();
    }

    /**
     * Lets a user leave a certain team
     *
     * @param user user that should leave the team
     * @param teamId id of the team that the user should leave
     */
    public void leaveTeam(User user, long teamId) {
        throw new UnsupportedOperationException();
    }
    //</editor-fold>

    private String generateToken() {
        return new BigInteger(130, random).toString(32);
    }
}
