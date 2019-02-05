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
public class FtpSystControl extends FtpControl {

	/**
	 * @param store
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
