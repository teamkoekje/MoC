package messaging;

/**
 * Simple interface used to indicate a class has a handler for when requests are
 * received.
 *
 * @param <REQUEST>
 *
 * @author TeamKoekje
 */
public interface IRequestListener<REQUEST> {

    public void receivedRequest(REQUEST request);
}
