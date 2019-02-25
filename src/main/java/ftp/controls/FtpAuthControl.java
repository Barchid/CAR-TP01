package ftp.controls;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 * 
 *         Represents the FTP AUTH command (that is not supported by the server,
 *         so it will send a 502 error)
 *
 */
public class FtpAuthControl extends FtpControl {

	/**
	 * @param store
	 */
	public FtpAuthControl(SessionStore store) {
		super(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ftp.controls.FtpControl#handle(ftp.FtpCommand)
	 */
	@Override
	public FtpReply handle(FtpCommand command) {
		return new FtpReply(5, 0, 2, "Explicit TLS authentication not allowed");
	}

}
