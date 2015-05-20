package main;

import javax.jms.JMSException;
import javax.naming.NamingException;
import messaging.BrokerGateway;

/**
 * Starts the server
 *
 * @author TeamKoekje
 */
public class StartServer {

    protected StartServer() {
    }

    /**
     * @param args the command line arguments
     * @throws javax.naming.NamingException
     * @throws javax.jms.JMSException
     */
    public static void main(String[] args) throws NamingException, JMSException  {
            BrokerGateway gtw = new BrokerGateway();
            System.out.println("Created gateway: " + gtw.toString());        
    }
}
