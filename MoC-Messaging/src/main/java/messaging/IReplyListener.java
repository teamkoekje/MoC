package messaging;

/**
 *
 * @author TeamKoekje
 * @param <REQUEST>
 * @param <REPLY>
 */
public interface IReplyListener<REQUEST, REPLY> {

    public void onReply(REQUEST request, REPLY reply);
}
