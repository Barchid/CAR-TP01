package ftp.controls;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import ftp.FtpCommand;
import ftp.FtpDataChannel;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID FTP control class used to manage the FTP command "STOR
 *         <filename>"
 *
 */
public class FtpStorDataControl extends FtpDataControl {

	/**
	 * @param store
	 * @param dataChannel
	 */
	public FtpStorDataControl(SessionStore store, FtpDataChannel dataChannel) {
		super(store, dataChannel);
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

		String toStorPath = command.getArg();
		if (toStorPath == null) {
			return new FtpReply(5, 0, 1, "Syntax error");
		}

		String parentPath = toStorPath.charAt(0) == '/' ? this.store.getRootDirectory()
				: this.store.getCurrentDirectory();

		if (toStorPath.charAt(0) == '/') {
			toStorPath.substring(1);
		}

		// in windows implementation, the path can contain a \, so we have to kill it
		if (toStorPath.length() != 0 && toStorPath.charAt(0) == '\\') {
			toStorPath = toStorPath.substring(1);
		}

		try {
			Path path = Paths.get(parentPath, toStorPath);
			System.out.println("Path of file to store is : " + path.toString());

			if (Files.isDirectory(path)) {
				return new FtpReply(5, 5, 0, "Filename invalid");
			}

			if (this.store.getTransferType() == SessionStore.TYPE_ASCII) {
				if (this.store.getActiveAdr() != null) {
					String data = this.dataChannel.readASCIIActive();

					if (data == null) {
						return null;
					}

					Files.write(path, data.getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
				} else {
					String data = this.dataChannel.readASCIIPassive();
					if (data == null) {
						return null;
					}

					Files.write(path, data.getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
				}
			} else {
				if (this.store.getActiveAdr() != null) {
					byte[] data = this.dataChannel.readImageActive();
					if (data == null) {
						return null;
					}

					Files.write(path, data, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
				} else {
					byte[] data = this.dataChannel.readImagePassive();
					if (data == null) {
						return null;
					}

					Files.write(path, data, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
				}
			}

			return new FtpReply(2, 2, 6, "Transfer complete");
		} catch (SecurityException ex) {
			return new FtpReply(5, 5, 0, "Permission denied");
		} catch (InvalidPathException ex) {
			return new FtpReply(5, 5, 0, "Syntax error : path not valid.");
		}
	}

}
