package postfix;

public class IncorrectFormatException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8238944295073154346L;

	public IncorrectFormatException() {

	}
	
	public IncorrectFormatException(String message) {
		super(message);
	}

}
