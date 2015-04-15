package messaging;

/**
 *
 * @author Maja Pesic
 * @param <REQUEST>
 */
public interface IRequestListener<REQUEST> {

    public void receivedRequest(REQUEST request);
}
