package util.exception;

/**
 *
 * @author Cloud
 */
public class CustomerNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CustomerNotFoundException</code> without
     * detail message.
     */
    public CustomerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CustomerNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
