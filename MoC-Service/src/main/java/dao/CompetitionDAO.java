package dao;

import domain.Competition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CompetitionDAO extends AbstractDAO<Competition> {

    @PersistenceContext
    private EntityManager em;

    public CompetitionDAO() {
        super(Competition.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
