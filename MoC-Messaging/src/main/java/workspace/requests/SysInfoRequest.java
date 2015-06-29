package workspace.requests;

/**
 * A Request used to tell a Workspace Server to retrieve it's System
 * Information.
 *
 * @author TeamKoekje
 */
public class SysInfoRequest extends Request {

    /**
     * Initializes a new instance of the SysInfoRequest class, used to retrieve the system information of a server.
     */
    public SysInfoRequest() {
        super(RequestAction.SYSINFO);
    }
}
