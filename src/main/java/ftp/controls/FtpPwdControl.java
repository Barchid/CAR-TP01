package ftp.controls;

import java.util.regex.Pattern;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 * 
 *         Control class used to manage the FTP command "PWD".
 *
 */
public class FtpPwdControl extends FtpControl {

	/**
	 * @param store the store of the client's connection
	 */
	public FtpPwdControl(SessionStore store) {
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

		// the path to display to the FTP client must be "/" instead of the root
		// directory.
		if (this.store.getCurrentDirectory().charAt(0) == '\\') {
			this.store.setCurrentDirectory(this.store.getCurrentDirectory().substring(1));
		}
		String displayDir = this.store.getCurrentDirectory().replaceFirst(Pattern.quote(this.store.getRootDirectory()),
				"/");

		return new FtpReply(2, 5, 7, "\"" + displayDir + "\" is current directory.");
	}

}
