package ftp;

/**
 * @author Sami BARCHID
 * Represents an FTP reply sent by the server when 
 */
public class FtpReply {
	private String code;
	private String message;

	/**
	 * 
	 * @param firstDigit  the first digit of the code
	 * @param secondDigit the second digit of the code
	 * @param thirdDigit  the third digit of the code
	 * @param message
	 */
	public FtpReply(int firstDigit, int secondDigit, int thirdDigit, String message) {
		super();
		this.code = firstDigit + "" + secondDigit + "" + thirdDigit;
		this.message = message;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 
	 * @param firstDigit  the first digit of the code
	 * @param secondDigit the second digit of the code
	 * @param thirdDigit  the third digit of the code
	 */
	public void setCode(int firstDigit, int secondDigit, int thirdDigit) {
		this.code = firstDigit + "" + secondDigit + "" + thirdDigit;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.code + " " + this.message + "\r\n";
	}
}
