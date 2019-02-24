package ftp.controls;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public class FtpCwdControl extends FtpControl {

	/**
	 * @param store
	 */
	public FtpCwdControl(SessionStore store) {
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

		String dirName = command.getArg();

		if (dirName == null) {
			this.store.setCurrentDirectory(this.store.getRootDirectory());
			return new FtpReply(2, 5, 0,
					"Broken client detected, missing argument to CWD. \"/\" is current directory.");
		}

		File newCurrentDir = this.findDirectory(dirName);

		if (newCurrentDir == null) {
			return new FtpReply(5, 5, 0, "CWD failed. \"" + command.getArg() + "\" : directory not found.");
		}

		this.store.setCurrentDirectory(newCurrentDir.getAbsolutePath());

		String displayDir = this.store.getCurrentDirectory().replaceFirst(Pattern.quote(this.store.getRootDirectory()),
				"/");

		return new FtpReply(2, 5, 0, "CWD successful. \"" + displayDir + "\" is current directory.");
	}

	/**
	 * Finds the directory located as a child or sub-child of the root directory of
	 * the FTP client.
	 * 
	 * @param dirName the directory name that is required.
	 * @return the directory found or null if the directory does not exist.
	 */
	private File findDirectory(String dirName) {
		// remove the first "/" that can be sent (not useful here)
		if (dirName.charAt(0) == '/') {
			dirName = dirName.substring(1);
		}

		// in windows implementation, the path can contain a \, so we have to kill it
		if (dirName.length() != 0 && dirName.charAt(0) == '\\') {
			dirName = dirName.substring(1);
		}
		try {
			File directory = Paths.get(this.store.getRootDirectory(), dirName).toFile();
			return directory;
		} catch (InvalidPathException ex) {
			return null;
		}
	}
}
