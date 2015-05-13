package websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    static Map<String, ArrayList<Session>> peers = new HashMap<>();

    private Session wsSession;

    @OnOpen
    public void openConnection(Session session) {
        if (session.getUserPrincipal() == null) {
            return;
        }
        addValues(session.getUserPrincipal().getName(), session);

        this.wsSession = session;

        send("New session started "
                + this.wsSession.getId()
                + " by user with username: "
                + session.getUserPrincipal().getName());
    }

    @OnClose
    public void closedConnection(Session session) {
        for (String key : peers.keySet()) {
            for (Session s : peers.get(key)) {
                if (s == session) {
                    peers.get(key).remove(session);
                }
            }
        }
    }

    public void sendToUser(String msg, String username) {
        try {
            for (Session session : peers.get(username)) {
                session.getBasicRemote().sendObject(msg);
            }
        } catch (EncodeException | IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        try {
            for (String key : peers.keySet()) {
                for (Session session : peers.get(key)) {
                    session.getBasicRemote().sendObject(msg);
                }
            }
        } catch (EncodeException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addValues(String key, Session value) {
        ArrayList tempList;
        if (peers.containsKey(key)) {
            tempList = peers.get(key);
            if (tempList == null) {
                tempList = new ArrayList();
            }
            tempList.add(value);
        } else {
            tempList = new ArrayList();
            tempList.add(value);
        }
        peers.put(key, tempList);
    }
}
