package ftp;

/**
 * @author Sami BARCHID
 * 
 *         Represents an FTP client's command sent in the control connection. A
 *         command is composed of a message and the option argument suuch as :
 *         "USER <username>"
 */
public class FtpCommand {
	private String message;
	private String arg;

	/**
	 * @param message
	 * @param arg
	 */
	public FtpCommand(String message, String arg) {
		super();
		this.message = message;
		this.arg = arg;
	}

	public FtpCommand(String message) {
		this(message, null);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the arg
	 */
	public String getArg() {
		return arg;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param arg the arg to set
	 */
	public void setArg(String arg) {
		this.arg = arg;
	}
}
