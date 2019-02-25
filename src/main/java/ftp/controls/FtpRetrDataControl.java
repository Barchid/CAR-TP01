package ftp.controls;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;

import ftp.FtpCommand;
import ftp.FtpDataChannel;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 * 
 *         Control class used to handle the FTP command "RETR".
 *
 */
public class FtpRetrDataControl extends FtpDataControl {

	/**
	 * @param store       the store of the client's connection
	 * @param dataChannel the service that can manage the data channel.
	 */
	public FtpRetrDataControl(SessionStore store, FtpDataChannel dataChannel) {
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

		// if there is no information about a port or passive mode
		if (this.store.getPassivePort() == -1 && this.store.getActiveAdr() == null) {
			return new FtpReply(5, 0, 3, "Bad sequence of commands.");
		}

		String fileName = command.getArg();
		if (fileName == null) {
			return new FtpReply(5, 0, 1, "Syntax error");
		}

		if (fileName.charAt(0) == '/') {
			fileName = fileName.substring(1);
		}

		File file = this.findFile(fileName);
		if (file == null) {
			return new FtpReply(5, 0, 0, "File cannot be sent");
		}

		this.sendFile(file);

		return new FtpReply(2, 2, 6, "File successfully transferred.");
	}

	/**
	 * Finds the file in the current directory with the same name as the name in
	 * parameter.
	 * 
	 * @param fileName the name of the file to find in the curent directory.
	 * @return the file instance of the file to find.
	 */
	private File findFile(String fileName) {
		File currentDir = new File(this.store.getCurrentDirectory());
		File[] files = currentDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});

		for (File file : files) {
			if (file.getName().equals(fileName)) {
				return file;
			}
		}

		return null;
	}

	/**
	 * Sends the content of the file in parameter to the FTP client by using the
	 * data channel.
	 * 
	 * @param file the file to transfer through the data channel
	 * @return true if the file has been sent or else false
	 * @throws IOException
	 */
	private boolean sendFile(File file) throws IOException {
		byte[] data = Files.readAllBytes(file.toPath());
		if (this.store.getTransferType() == SessionStore.TYPE_ASCII) {
			if (this.store.getActiveAdr() != null) {
				this.dataChannel.sendASCIIActive(new String(data));
			} else if (this.store.getPassivePort() != -1) {
				this.dataChannel.sendASCIIPassive(new String(data));
			} else {
				// there is no active or passive mode set, so file cannot be sent.
				return false;
			}
		} else {
			if (this.store.getActiveAdr() != null) {
				this.dataChannel.sendImageActive(data);
			} else if (this.store.getPassivePort() != -1) {
				this.dataChannel.sendImagePassive(data);
			} else {
				// there is no mode for transfer file set. File cannot be sent.
				return false;
			}
		}
		return true;
	}
}
