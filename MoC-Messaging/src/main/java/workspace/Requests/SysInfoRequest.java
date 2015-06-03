package workspace.Requests;

/**
 * A Request used to tell a Workspace Server to retrieve it's System
 * Information.
 *
 * @author TeamKoekje
 */
public class SysInfoRequest extends Request {

    public SysInfoRequest(Action action) {
        super(action);
    }
}
