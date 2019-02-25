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
 *         Control class used to manage a RNFR FTP command.
 */
public class FtpRnfrControl extends FtpControl {

	/**
	 * @param store store of the client communication
	 */
	public FtpRnfrControl(SessionStore store) {
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

		String fromPath = command.getArg();
		if (fromPath == null) {
			return new FtpReply(5, 0, 1, "Syntax error");
		}

		String parentPath = fromPath.charAt(0) == '/' ? this.store.getRootDirectory()
				: this.store.getCurrentDirectory();

		if (fromPath.charAt(0) == '/') {
			fromPath = fromPath.substring(1);
		}

		// in windows implementation, the path can contain a \, so we have to kill it
		if (fromPath.length() != 0 && fromPath.charAt(0) == '\\') {
			fromPath = fromPath.substring(1);
		}

		try {
			Path path = Paths.get(parentPath, fromPath);
			if (Files.exists(path)) {
				this.store.setRenameFrom(path.toAbsolutePath().toString());
				if (Files.isDirectory(path)) {
					return new FtpReply(3, 5, 0, "Directory exists, ready for destination name");
				} else {
					return new FtpReply(3, 5, 0, "File exists, ready for destination name.");
				}
			} else {
				return new FtpReply(5, 0, 0, "Rename FROM does not exist.");
			}
		} catch (SecurityException ex) {
			return new FtpReply(5, 5, 0, "Permission denied");
		} catch (InvalidPathException ex) {
			return new FtpReply(5, 5, 0, "Syntax error : path not valid.");
		}
	}

}
