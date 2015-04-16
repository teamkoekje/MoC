/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.logging.Level;
import java.util.logging.Logger;
import messaging.BrokerGateway;
import messaging.JMSSettings;

/**
 *
 * @author Robin
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            BrokerGateway gtw = new BrokerGateway(JMSSettings.BROKER_REQUEST, JMSSettings.WORKSPACE_REPLY, JMSSettings.WORKSPACE_REQUEST);
            gtw.start();
        } catch (Exception ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
