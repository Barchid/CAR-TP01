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
public class FtpPassControl extends FtpControl {

	/**
	 * @param store
	 */
	public FtpPassControl(SessionStore store) {
		super(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ftp.controls.FtpControl#handle(ftp.FtpCommand)
	 */
	@Override
	public FtpReply handle(FtpCommand command) {
		if (this.store.isLoggedIn()) {
			return new FtpReply(5, 0, 3, "Bad sequence of commands.");
		}

		if (this.store.getUsername().isEmpty()) {
			return new FtpReply(5, 3, 0, "Login or password incorrect!");
		}

		String password = command.getArg();

		if (password == null || !password.equals("sami")) {
			return new FtpReply(5, 3, 0, "Login or password incorrect!");
		}

		this.store.setPassword(password);
		this.store.setLoggedIn(true);
		return new FtpReply(2, 3, 0, "Logged on");
	}

}
