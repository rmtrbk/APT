package au.com.ap.aptouch.exception;

/**
 * Exception denoting malformed transaction input
 * 
 * @author Theja Kotuwella
 *
 */
public class TransactionFormatException extends Exception {
	private static final long serialVersionUID = 1L;

	public TransactionFormatException(String errorMessage) {
        super(errorMessage);
    }
}
