package main;

import messaging.BrokerGateway;

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
