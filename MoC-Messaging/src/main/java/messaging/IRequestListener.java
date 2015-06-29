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
    /**
     * Handler for when a REQUEST is received.
     * @param request The received request.
     */
    public void receivedRequest(REQUEST request);
}
