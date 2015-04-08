package dao;

import domain.Round;
import domain.Team;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class TeamDAO extends AbstractDAO<Team> {

    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeamDAO() {
        super(Team.class);
    }

    /**
     * Find all teams that belong to a certain competition
     *
     * @param competitionId the id of the competition
     * @return list with teams that belong to the competition
     */
    public List<Team> findByCompetition(long competitionId) {
        Query q = em.createNamedQuery("Team.findByCompetition");
        q.setParameter("competitionId", competitionId);
        return q.getResultList();
    }
}
