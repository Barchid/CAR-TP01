package ftp.controls;

import java.util.regex.Pattern;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public class FtpPwdControl extends FtpControl {

	/**
	 * @param store
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
		String displayDir = this.store.getCurrentDirectory().replaceFirst(Pattern.quote(this.store.getRootDirectory()),
				"/");

		return new FtpReply(2, 5, 7, "\"" + displayDir + "\" is current directory.");
	}

}
