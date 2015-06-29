package workspace.requests;

/**
 * Indicates the action for the request. This is used on the workspace servers
 * to cast the request to the correct sub-request.
 *
 * @author TeamKoekje
 */
public enum RequestAction {

    /**
     * Indicates an Action where the server should compile.
     */
    COMPILE,
    /**
     * Indicates an Action where a specific test should be run.
     */
    TEST,
    /**
     * Indicates an Action where all tests should be run.
     */
    TESTALL,
    /**
     * Indicates an Action where a file should be updated with new content.
     */
    UPDATE,
    /**
     * Indicates an Action where a new Workspace should be created.
     */
    CREATE,
    /**
     * Indicates an Action where a Workspace should be removed.
     */
    DELETE,
    /**
     * Indicates an Action where a challenge should be pushed, extracted and
     * scanned to all workspace servers.
     */
    PUSH_CHALLENGE,
    /**
     * Indicates an Action where the folder/file structure of a challenge should
     * be retrieved.
     */
    FOLDER_STRUCTURE,
    /**
     * Indicates an Action where the contents of a file should be retrieved.
     */
    FILE,
    /**
     * Indicates an Action where the System Information should be retrieved.
     */
    SYSINFO,
    /**
     * Indicates an Action where the Tests available to Participants should be
     * retrieved.
     */
    AVAILABLE_TESTS
}
