package domain;

import java.util.List;
import org.apache.commons.lang.NotImplementedException;

public class AccountManager {

    private List<Manager> managers;
    private List<Participant> participants;

    private static AccountManager instance;

    private AccountManager() {
    }

    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    /**
     * Removes an account with the specified user.
     *
     * @param user The user to remove.
     */
    public void RemoveAccount(User user) {
        if (user instanceof Participant) {
            participants.remove((Participant) user);
        } else if (user instanceof Manager) {
            managers.remove((Manager) user);
        } else {
            throw new IllegalArgumentException("Unknown user implementation");
        }
    }

    /**
     * Creates an account with the specified AccountType.
     *
     * @param type The type of the account to create.
     */
    public void CreateAccount(AccountType type) {
        switch (type) {
            case ADMINISTRATOR:
                //create admin
                break;
            case PARTICIPANT:
                //create participant
                break;
            default:
                throw new NotImplementedException();
        }
    }

    /**
     * An enum indicating the possible account types.
     */
    public enum AccountType {

        PARTICIPANT, ADMINISTRATOR
    }
}
