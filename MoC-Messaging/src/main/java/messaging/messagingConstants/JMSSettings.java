package messaging.messagingConstants;
/**
 * Class providing Strings for naming purposes of JMS.
 * 
 * @author TeamKoekje
 */
public class JMSSettings {

    protected JMSSettings(){}
    
    /**
     * Custom activeMQ server IP.
     */
    public static String IP = "192.168.24.34";
    //static String IP = "localhost";

    /**
     * URL of the ActiveMQ connection
     */
    public static final String URL_ACTIVE_MQ = "tcp://" + IP + ":61616?jms.prefetchPolicy.queuePrefetch=10";
    /**
     * The name for the queue used for requests to the workspace.
     */
    public static final String WORKSPACE_REQUEST = "WORKSPACE_REQUEST";
    /**
     * The name of the queue used for replies to the broker.
     */
    public static final String BROKER_REPLY = "BROKER_REPLY";

    /**
     * The name of the queue used to send init requests from workspaces to the service.
     */
    public static final String WORKSPACE_INIT_REPLY = "WORKSPACE_INIT_REPLY";
    /**
     * The name of the queue used to send init replies from the service to the workspaces.
     */
    public static final String BROKER_INIT_REQUEST = "BROKER_INIT_REQUEST";
}
