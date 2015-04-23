package main;

import java.util.logging.Level;
import java.util.logging.Logger;
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
     */
    public static void main(String[] args) {
        try {
            BrokerGateway gtw = new BrokerGateway();
            System.out.println("Created gateway: " + gtw.toString());
        } catch (Exception ex) {
            Logger.getLogger(StartServer.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
        }
    }
}
