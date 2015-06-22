package websocket;

// <editor-fold defaultstate="collapsed" desc="Imports" >
import com.sun.media.jfxmedia.logging.Logger;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
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

    static Map<String, Session> peers = new HashMap<>();

    @OnOpen
    public void openConnection(Session session) {
        //System.out.println("Client opened websocket");
        Principal p = session.getUserPrincipal();
        if (p != null) {
            Logger.logMsg(Logger.INFO, "Client opened websocket: " + p.getName());
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
        Logger.logMsg(Logger.INFO, "Sending message to: " + username);
        Session s = peers.get(username);
        Async b = s.getAsyncRemote();
        b.sendText(msg);
    }
}
