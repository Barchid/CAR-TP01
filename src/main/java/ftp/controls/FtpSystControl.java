package ftp.controls;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 *         Control class used to manage the FTP command "SYST".
 */
public class FtpSystControl extends FtpControl {

	/**
	 * @param store the store of the client communication
	 */
	public FtpSystControl(SessionStore store) {
		super(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ftp.controls.FtpControl#handle(ftp.FtpCommand)
	 */
	@Override
	public FtpReply handle(FtpCommand command) {
		return new FtpReply(2, 1, 5, "JVM emulated by Barchid.");
	}

}
