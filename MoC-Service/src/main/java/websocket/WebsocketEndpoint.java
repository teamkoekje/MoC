package websocket;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import java.security.Principal;
import java.util.HashMap;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
// </editor-fold>

/**
 * Websocket used to send messages from the Service to Clients.
 *
 * @author TeamKoekje
 */
@ServerEndpoint(value = "/ws/api")
public class WebsocketEndpoint {

    static HashMap<String, Session> peers = new HashMap<>();

    @OnOpen
    public void openConnection(Session session) {
        //System.out.println("Client opened websocket");
        Principal p = session.getUserPrincipal();
        if (p != null) {
                    System.out.println("Client opened websocket: " + p.getName());

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
        System.out.println(s.toString());
        Async a = s.getAsyncRemote();
        System.out.println("temp");
        a.sendObject(msg);
        System.out.println("Message sent to user: " + username);
    }
}
