package ftp.controls;

import java.io.IOException;

import ftp.FtpCommand;
import ftp.FtpControlChannel;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public class FtpUnknownControl extends FtpControl {

	/**
	 * @param store
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
