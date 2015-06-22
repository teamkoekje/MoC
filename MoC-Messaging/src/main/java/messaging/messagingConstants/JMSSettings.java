package messaging.messagingConstants;
/**
 * Class providing Strings for naming purposes of JMS.
 * 
 * @author TeamKoekje
 */
public class JMSSettings {

    protected JMSSettings(){
        
    }
    
    //static String IP = "192.168.24.34";
    //static String IP = "localhost";

    public static final String URL_ACTIVE_MQ = "tcp://localhost:61616?jms.prefetchPolicy.queuePrefetch=10";
    public static final String WORKSPACE_REQUEST = "WORKSPACE_REQUEST";
    public static final String BROKER_REPLY = "BROKER_REPLY";

    public static final String WORKSPACE_INIT_REPLY = "WORKSPACE_INIT_REPLY";
    public static final String BROKER_INIT_REQUEST = "BROKER_INIT_REQUEST";
}
