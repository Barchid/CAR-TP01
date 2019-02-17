package ftp.controls;

import java.io.IOException;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public abstract class FtpControl {
	protected SessionStore store;

	/**
	 * @param store
	 */
	public FtpControl(SessionStore store) {
		super();
		this.store = store;
	}

	/**
	 * Handles the received command and returns the adequate reply. This method must
	 * be overridden by any FTP controller to manage the command in the right way.
	 * 
	 * @param command the command to handle
	 * @return FtpReply the adequate reply.
	 * @throws IOException
	 */
	public abstract FtpReply handle(FtpCommand command) throws IOException;
}
