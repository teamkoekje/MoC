package workspace;

/**
 * This class represents an object that tells a specific workspace to perform a
 * specific action
 *
 * @author TeamKoekje
 */
public class ZipRequest extends Request {

    private final byte[] data;


    public ZipRequest(byte[] data) {
        super(Action.UPLOAD_CHALLENGE);
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
