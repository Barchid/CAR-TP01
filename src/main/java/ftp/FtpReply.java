package ftp;

/**
 * @author Sami BARCHID
 * 
 *         Represents an FTP reply sent by the server after a command of the
 *         client. An FTP reply is composed by the a three digit code and an
 *         additionnal message. For example :
 * 
 *         "550 network error"
 */
public class FtpReply {
	private String code;
	private String message;

	/**
	 * 
	 * @param firstDigit  the first digit of the code
	 * @param secondDigit the second digit of the code
	 * @param thirdDigit  the third digit of the code
	 * @param message     the additionnal message of the reply.
	 */
	public FtpReply(int firstDigit, int secondDigit, int thirdDigit, String message) {
		super();
		this.code = firstDigit + "" + secondDigit + "" + thirdDigit;
		this.message = message;
	}

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

	public String getMessage() {
		return message;
	}

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
