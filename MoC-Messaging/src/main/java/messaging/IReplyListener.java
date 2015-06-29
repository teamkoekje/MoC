package messaging;

/**
 * Simple interface used to indicate a class has a handler for when replies are
 * received.
 *
 * @author TeamKoekje
 * @param <REQUEST>
 * @param <REPLY>
 */
public interface IReplyListener<REQUEST, REPLY> {

    /**
     * Handler for when a Reply is received.
     * @param request The received REQUEST.
     * @param reply The received REPLY.
     */
    public void onReply(REQUEST request, REPLY reply);
}
