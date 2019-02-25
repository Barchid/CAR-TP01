package ftp.controls;

import java.io.IOException;
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
 *         Ftp control class used to manage the FTP control command "RNTO"
 */
public class FtpRntoControl extends FtpControl {

	/**
	 * @param store store of the client communication
	 */
	public FtpRntoControl(SessionStore store) {
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

		if (this.store.getRenameFrom() == null) {
			return new FtpReply(5, 0, 0, "Bad sequence of commands. Make a RNFR before afouh.");
		}

		String toPath = command.getArg();
		if (toPath == null) {
			return new FtpReply(5, 0, 1, "Syntax error");
		}

		String parentPath = toPath.charAt(0) == '/' ? this.store.getRootDirectory() : this.store.getCurrentDirectory();

		if (toPath.charAt(0) == '/') {
			toPath = toPath.substring(1);
		}

		// in windows implementation, the path can contain a \, so we have to kill it
		if (toPath.length() != 0 && toPath.charAt(0) == '\\') {
			toPath = toPath.substring(1);
		}

		try {
			Path path = Paths.get(parentPath, toPath);
			if (Files.exists(path)) {
				return new FtpReply(5, 5, 0, "Destination already exists.");
			} else {
				Files.move(Paths.get(this.store.getRenameFrom()), path);
				this.store.setRenameFrom(null);
				return new FtpReply(2, 5, 0, "File renamed successfully.");
			}
		} catch (SecurityException ex) {
			return new FtpReply(5, 5, 0, "Permission denied");
		} catch (InvalidPathException ex) {
			return new FtpReply(5, 5, 0, "Syntax error : path not valid.");
		}
	}

}
