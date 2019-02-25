package ftp.controls;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 * 
 *         Controller used for the FTP command "MKD [directory path]"
 */
public class FtpMkdControl extends FtpControl {

	/**
	 * @param store the store of the client's connection
	 */
	public FtpMkdControl(SessionStore store) {
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

		String dirPath = command.getArg();
		if (dirPath == null) {
			return new FtpReply(5, 0, 1, "Syntax error");
		}
		String parentPath = dirPath.charAt(0) == '/' ? this.store.getRootDirectory() : this.store.getCurrentDirectory();

		if (dirPath.charAt(0) == '/') {
			dirPath = dirPath.substring(1);
		}

		// in windows implementation, the path can contain a \, so we have to kill it
		if (dirPath.length() != 0 && dirPath.charAt(0) == '\\') {
			dirPath = dirPath.substring(1);
		}

		try {
			Files.createDirectories(Paths.get(parentPath, dirPath));
		} catch (FileAlreadyExistsException e) {
			if (Files.isDirectory(Paths.get(parentPath, dirPath))) {
				return new FtpReply(5, 5, 0, "Directory already exists.");
			} else {
				return new FtpReply(5, 5, 0, "File with same name already exists.");
			}
		} catch (InvalidPathException ex) {
			return new FtpReply(5, 0, 1, "Syntax error : invalid path");
		} catch (SecurityException ex) {
			return new FtpReply(5, 5, 0, "Permission denied");
		}

		return new FtpReply(2, 5, 7, dirPath + " created successfully");
	}
}
