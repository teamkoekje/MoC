package messaging;

/**
 * @author TeamKoekje
 * @param <REQUEST>
 */
public interface IRequestListener<REQUEST> {

    public void receivedRequest(REQUEST request);
}
