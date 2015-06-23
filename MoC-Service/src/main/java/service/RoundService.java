package service;

import domain.Round;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

/**
 * Service class used to manage rounds of a competition
 *
 * @author TeamKoekje
 */
@Stateless
@RequestScoped
public class RoundService extends GenericService<Round> {

    @Inject
    private CompetitionService competitionService;
    
    public RoundService() {
        super(Round.class);
    }

    /**
     * Find all rounds that belong to a certain competition
     *
     * @param competitionId the id of the competition
     * @return list with rounds that belong to the competition
     */
    public List<Round> findByCompetition(long competitionId) {
        return competitionService.findById(competitionId).getRounds();
    }
}
