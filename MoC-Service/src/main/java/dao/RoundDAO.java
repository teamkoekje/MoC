package dao;

import domain.Round;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RoundDAO extends AbstractDAO<Round> {

    @PersistenceContext
    private EntityManager em;

    public RoundDAO() {
        super(Round.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    /**
     * Find all rounds that belong to a certain competition
     *
     * @param competitionId the id of the competition
     * @return list with rounds that belong to the competition
     */
    public List<Round> findByCompetition(long competitionId) {
        Query q = em.createNamedQuery("Round.findByCompetition");
        q.setParameter("competitionId", competitionId);
        return q.getResultList();
    }

}
