package ftp.controls;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
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

		// remove the "/" if it is the first char in the dir name
		if (dirName.charAt(0) == '/') {
			dirName = dirName.substring(1);
		}

		File newCurrentDir = this.findSubDirectory(dirName);

		if (newCurrentDir == null) {
			return new FtpReply(5, 5, 0, "CWD failed. \"/" + command.getArg() + "\" : directory not found.");
		}

		this.store.setCurrentDirectory(newCurrentDir.getAbsolutePath());

		String displayDir = this.store.getCurrentDirectory().replaceFirst(Pattern.quote(this.store.getRootDirectory()),
				"/");

		return new FtpReply(2, 5, 0, "CWD successful. \"" + displayDir + "\" is current directory.");
	}

	/**
	 * Finds the subdirectory of the current directory that has the name defined in
	 * parameter.
	 * 
	 * @param subDirName the name of the sub directory to find
	 * @return the subdirectory to find.
	 */
	private File findSubDirectory(String subDirName) {
		File currentDir = new File(this.store.getCurrentDirectory());

		// gets all the sub directory of the current dir
		File[] subDirs = currentDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});

		for (File subDir : subDirs) {
			if (subDir.getName().equals(subDirName)) {
				return subDir;
			}
		}

		return null;
	}

}
