package ftp.controls;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public class FtpTypeControl extends FtpControl {
	/**
	 * @param store
	 */
	public FtpTypeControl(SessionStore store) {
		super(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ftp.controls.FtpControl#handle(ftp.FtpCommand)
	 */
	@Override
	public FtpReply handle(FtpCommand command) {
		if (!this.store.isLoggedIn()) {
			return new FtpReply(5, 3, 0, "Please log in with USER and PASS first.");
		}

		String typeArg = command.getArg();
		int typeFormat = this.typeOfArg(typeArg);
		if (typeFormat == -1) {
			return new FtpReply(5, 0, 1, "Syntax error.");
		}

		this.store.setTransferType(typeFormat);

		return new FtpReply(2, 0, 0, "Type set to " + typeArg);
	}

	/**
	 * Extracts the type of file to be transferred by the data channel. Please refer
	 * to http://www.nsftools.com/tips/RawFTP.htm#TYPE for more information
	 * 
	 * @param typeArg the argument to validate
	 * @return 1 if typeArg is related to the ASCII type format, 2 if it is related
	 *         to the IMAGE type format or else -1 when not valid or not supported.
	 */
	private int typeOfArg(String typeArg) {
		if (typeArg == null) {
			return -1;
		}
		if (typeArg.equals("ASCII") || typeArg.equals("A")) {
			return SessionStore.TYPE_ASCII;
		}

		if (typeArg.equals("I")) {
			return SessionStore.TYPE_IMAGE;
		}
		return -1;
	}

}
