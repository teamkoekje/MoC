package websocket;

import java.security.Principal;
import java.util.HashMap;
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

    static HashMap<String, Session> peers = new HashMap<>();

    @OnOpen
    public void openConnection(Session session) {
        System.out.println("Client opened websocket");
        Principal p = session.getUserPrincipal();
        if (p != null) {
            peers.put(p.getName(), session);
        }
    }

    @OnClose
    public void closedConnection(Session session) {
        Principal p = session.getUserPrincipal();
        if (p != null) {
            peers.remove(p.getName());
        }
    }

    public void broadCast(String msg) {
        for (Session s : peers.values()) {
            s.getAsyncRemote().sendObject(msg);
        }
    }

    public void sendToUser(String msg, String username) {
        System.out.println("Sending message to: " + username);
        Session s = peers.get(username);
        s.getAsyncRemote().sendObject(msg);
        System.out.println("Message sent to user: " + username);
    }
}
