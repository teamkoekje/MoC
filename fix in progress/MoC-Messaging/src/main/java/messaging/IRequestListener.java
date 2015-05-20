package messaging;

/**
 * @param <REQUEST>
 * 
 * @author TeamKoekje
 */
public interface IRequestListener<REQUEST> {

    public void receivedRequest(REQUEST request);
}
