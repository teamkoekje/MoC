/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import workspace.Broker;
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
        Broker broker = new Broker(JMSSettings.BROKER_REQUEST, JMSSettings.WORKSPACE_REQUEST, JMSSettings.BROKER_RERPLY);
        broker.start();
    }
}
