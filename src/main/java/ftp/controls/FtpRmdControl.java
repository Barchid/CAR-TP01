package ftp.controls;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 * 
 *         FTP control class used to manage the FTP command "RMD directory"
 */
public class FtpRmdControl extends FtpControl {

	/**
	 * @param store the store of the client's connection
	 */
	public FtpRmdControl(SessionStore store) {
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

		String toDeletePath = command.getArg();
		if (toDeletePath == null) {
			return new FtpReply(5, 0, 1, "Syntax error");
		}

		String parentPath = toDeletePath.charAt(0) == '/' ? this.store.getRootDirectory()
				: this.store.getCurrentDirectory();

		if (toDeletePath.charAt(0) == '/') {
			toDeletePath = toDeletePath.substring(1);
		}

		// in windows implementation, the path can contain a \, so we have to kill it
		if (toDeletePath.length() != 0 && toDeletePath.charAt(0) == '\\') {
			toDeletePath = toDeletePath.substring(1);
		}

		try {
			Path path = Paths.get(parentPath, toDeletePath);
			if (!Files.exists(path)) {
				return new FtpReply(5, 5, 0, "File not found.");
			} else if (!Files.isDirectory(path)) {
				return new FtpReply(5, 5, 0, "Destination is not a directory.");
			} else {
				Files.delete(path);
				return new FtpReply(2, 5, 0, "File deleted successfully.");
			}
		} catch (SecurityException ex) {
			return new FtpReply(5, 5, 0, "Permission denied");
		} catch (InvalidPathException ex) {
			return new FtpReply(5, 5, 0, "Syntax error : path not valid.");
		} catch (DirectoryNotEmptyException ex) {
			return new FtpReply(5, 5, 0, "Directory not empty.");
		}
	}

}
