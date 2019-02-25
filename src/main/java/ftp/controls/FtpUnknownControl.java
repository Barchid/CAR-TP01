package ftp.controls;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 *         Control class used to manage the FTP commands unknown by the server.
 */
public class FtpUnknownControl extends FtpControl {

	/**
	 * @param store the store
	 */
	public FtpUnknownControl(SessionStore store) {
		super(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ftp.controls.FtpControl#handle(ftp.FtpCommand)
	 */
	@Override
	public FtpReply handle(FtpCommand command) {
		return new FtpReply(5, 0, 0, "Syntax error, command unrecognized.");
	}

}
