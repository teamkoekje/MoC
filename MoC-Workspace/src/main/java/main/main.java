package main;

import java.util.logging.Level;
import java.util.logging.Logger;
import messaging.BrokerGateway;
import messaging.JMSSettings;

/**
 * //TODO: class description, what does this class do
 *
 * @author TeamKoekje
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            BrokerGateway gtw = new BrokerGateway();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

}
