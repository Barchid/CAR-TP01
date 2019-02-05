package ftp.controls;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public class FtpUserControl extends FtpControl {

	/**
	 * @param store
	 */
	public FtpUserControl(SessionStore store) {
		super(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ftp.controls.FtpControl#handle(ftp.FtpCommand)
	 */
	@Override
	public FtpReply handle(FtpCommand command) {
		String username = command.getArg();

		if (username == null) {
			return new FtpReply(5, 0, 1, "Syntax error");
		}

		// IF [already logged in] : forget the current user
		if (this.store.isLoggedIn()) {
			this.store.setLoggedIn(false);
			this.store.setPassword("");
		}

		this.store.setUsername(username);
		return new FtpReply(3, 3, 1, "Password required for " + username);
	}

}
