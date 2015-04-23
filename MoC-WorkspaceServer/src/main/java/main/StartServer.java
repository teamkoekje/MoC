package main;

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
     * @throws java.lang.Exception When something goes wrong while creating the gateway
     */
    public static void main(String[] args) throws Exception {
            BrokerGateway gtw = new BrokerGateway();
            System.out.println("Created gateway: " + gtw.toString());        
    }
}
