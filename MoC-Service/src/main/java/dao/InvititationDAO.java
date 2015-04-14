package dao;

import domain.Invitation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class InvititationDAO extends AbstractDAO<Invitation> {

    @PersistenceContext
    private EntityManager em;

    public InvititationDAO() {
        super(Invitation.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
