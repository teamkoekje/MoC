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
 * Websocket used to send messages from the Service to Clients. TODO: A type of
 * guaranteed delivery.
 *
 * @author TeamKoekje
 */
@ServerEndpoint(value = "/ws/api")
public class WebsocketEndpoint {

    private static final Map<String, Session> peers = new HashMap<>();

    /**
     * Handler for when a connection request is received and opened. Makes sure
     * the Session is added to the internal list of connected users.
     *
     * @param session The newly opened Session.
     */
    @OnOpen
    public void openConnection(Session session) {
        Principal p = session.getUserPrincipal();
        if (p != null) {
            Logger.logMsg(Logger.INFO, "Client opened websocket: " + p.getName());
            peers.put(p.getName(), session);
        }
    }

    /**
     * Handler for when a Session closes, which makes sure the session is
     * removed from the internal list of connected users.
     *
     * @param session The closed Session.
     */
    @OnClose
    public void closedConnection(Session session) {
        Principal p = session.getUserPrincipal();
        if (p != null) {
            peers.remove(p.getName());
        }
    }

    /**
     * Broadcasts the specified message to all connected clients.
     *
     * @param msg The message to broadcast.
     */
    public void broadCast(String msg) {
        for (Session s : peers.values()) {
            s.getAsyncRemote().sendObject(msg);
        }
    }

    /**
     * Sends a message to the specified user.
     *
     * @param msg The message to send
     * @param username The name of the user to send to.
     */
    public void sendToUser(String msg, String username) {
        Logger.logMsg(Logger.INFO, "Sending message to: " + username);
        Session s = peers.get(username);
        Async b = s.getAsyncRemote();
        b.sendText(msg);
    }
}
