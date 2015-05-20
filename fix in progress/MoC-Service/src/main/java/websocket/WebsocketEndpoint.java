package websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Robin
 */
@ServerEndpoint(value = "/ws/api")
public class WebsocketEndpoint {
    
    static HashMap<Session, String> peers = new HashMap<>();
 
    @OnOpen
    public void openConnection(Session session) {
        try {
            if (session.getUserPrincipal() == null) {
                return;
            }
            if (!peers.containsKey(session)) {
                peers.put(session, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 
    @OnClose
    public void closedConnection(Session session) {
        if (peers.containsKey(session)) {
            peers.remove(session);
        }
    }
 
    public static void broadCast(String msg) {
        try {
            Iterator<Session> keySetIterator = peers.keySet().iterator();

            while(keySetIterator.hasNext()){
                Session s = keySetIterator.next();
                s.getBasicRemote().sendObject(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendToUser(String msg, String correlation) {
        try {
            Iterator<Session> keySetIterator = peers.keySet().iterator();

            while(keySetIterator.hasNext()){
                Session s = keySetIterator.next();
                if(peers.get(s).equals(correlation)){
                    s.getBasicRemote().sendObject(msg);
                }
            }
        } catch (EncodeException | IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setCorrelationId(String username, String correlation){
        Iterator<Session> keySetIterator = peers.keySet().iterator();

        while(keySetIterator.hasNext()){
            Session s = keySetIterator.next();
            if(s.getUserPrincipal().getName().equals(username)){
                peers.replace(s, correlation);
            }
        }
    }
}
