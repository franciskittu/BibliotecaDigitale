package it.biblio.framework.data;

/**
 * 
 * @author francesco
 *
 */
public class DataLayerException extends Exception {

	public DataLayerException(String message) {
		super(message);
	}

	public DataLayerException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataLayerException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return super.getMessage() + (getCause() != null ? " (" + getCause().getMessage() + ")" : "");
	}
}
