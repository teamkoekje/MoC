package service;

import dao.AbstractDAO;
import dao.RoundDAO;
import domain.Round;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

/**
 * Service class used to manage rounds of a competition
 *
 * @author Astrid
 */
@Stateless
@RequestScoped
public class RoundService extends AbstractService<Round> {

    @Inject
    private RoundDAO dao;

    @Override
    protected AbstractDAO getDAO() {
        return dao;
    }

    /**
     * Find all rounds that belong to a certain competition
     *
     * @param competitionId the id of the competition
     * @return list with rounds that belong to the competition
     */
    public List<Round> findByCompetition(long competitionId) {
        return dao.findByCompetition(competitionId);
    }
}
