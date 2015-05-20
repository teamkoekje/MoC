package service;

import domain.Competition;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;

@Stateless
@RequestScoped
public class CompetitionService extends GenericService<Competition> {

    /*
    - 1 Timer update alle lopende competities.
    - Lopende competities worden bij het opstarten van de service opgehaald en in een lijstje gestopt
    */
    public CompetitionService() {
        super(Competition.class);
    }

    public boolean joinTeam(String email, String token, long competitionId, long teamId) {
        Competition c = this.findById(competitionId);
        boolean result = c.joinTeam(email, token, teamId);
        if (result) {
            this.edit(c);
        }
        return result;
    }

}
