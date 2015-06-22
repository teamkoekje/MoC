package workspace.requests2;

/**
 * Indicates the action for the request. This is used on the workspace servers
 * to cast the request to the correct sub-request.
 *
 * @author TeamKoekje
 */
public enum RequestAction {

    COMPILE, TEST, TESTALL, UPDATE, CREATE, DELETE, PUSH_CHALLENGE, FOLDER_STRUCTURE, FILE, SYSINFO, AVAILABLE_TESTS
}
