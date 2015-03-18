package domain;

//@Author Casper
import java.util.List;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class AccountManager {

    private List<Manager> managers;
    private List<Participant> participants;

    private static AccountManager instance;

    private AccountManager() {
        //load managers & participants
    }

    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    /**
     * Creates an account with the specified AccountType.
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
}
