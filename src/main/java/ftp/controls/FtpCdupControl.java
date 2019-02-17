package ftp.controls;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public class FtpCdupControl extends FtpControl {

	/**
	 * @param store
	 */
	public FtpCdupControl(SessionStore store) {
		super(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ftp.controls.FtpControl#handle(ftp.FtpCommand)
	 */
	@Override
	public FtpReply handle(FtpCommand command) throws IOException {
		if (!this.store.isLoggedIn()) {
			return new FtpReply(5, 3, 0, "Please log in with USER and PASS first.");
		}

		// change directory if it is not the root directory.
		if (!this.store.getCurrentDirectory().equals(this.store.getRootDirectory())) {
			File dir = new File(this.store.getCurrentDirectory());

			this.store.setCurrentDirectory(dir.getParentFile().getAbsolutePath());
		}

		// the path to display to the FTP client must be "/" instead of the root
		// directory.
		String displayDir = this.store.getCurrentDirectory().replaceFirst(Pattern.quote(this.store.getRootDirectory()),
				"/");

		return new FtpReply(2, 0, 0, "CDUP successful. \"" + displayDir + "\" is current directory.");
	}

}
