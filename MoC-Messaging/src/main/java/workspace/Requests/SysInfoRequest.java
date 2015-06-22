package workspace.requests;

/**
 * A Request used to tell a Workspace Server to retrieve it's System
 * Information.
 *
 * @author TeamKoekje
 */
public class SysInfoRequest extends Request {

    public SysInfoRequest(RequestAction action) {
        super(action);
    }
}
