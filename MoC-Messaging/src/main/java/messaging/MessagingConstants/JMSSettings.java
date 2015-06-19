package messaging.MessagingConstants;
/**
 * Class providing Strings for naming purposes of JMS.
 * 
 * @author TeamKoekje
 */
public class JMSSettings {

    //public static final String URL_ACTIVE_MQ = "tcp://localhost:61616?jms.prefetchPolicy.queuePrefetch=10"; //LOCAL
    public static final String URL_ACTIVE_MQ = "tcp://192.168.24.34:61616?jms.prefetchPolicy.queuePrefetch=10"; //SERVER

    public static final String WORKSPACE_REQUEST = "WORKSPACE_REQUEST";
    public static final String BROKER_REPLY = "BROKER_REPLY";

    public static final String WORKSPACE_INIT_REPLY = "WORKSPACE_INIT_REPLY";
    public static final String BROKER_INIT_REQUEST = "BROKER_INIT_REQUEST";
}
