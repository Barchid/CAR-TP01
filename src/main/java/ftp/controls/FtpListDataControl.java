package ftp.controls;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import ftp.FtpCommand;
import ftp.FtpDataChannel;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 *         Control class used to handle the command "LIST"
 */
public class FtpListDataControl extends FtpDataControl {

	/**
	 * @param store       the store of the client's connection
	 * @param dataChannel the data channel service to handle the data connection
	 *                    with the client.
	 */
	public FtpListDataControl(SessionStore store, FtpDataChannel dataChannel) {
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

		String data = this.listDirectory();

		int passivePort = this.store.getPassivePort();
		if (passivePort == -1) { // if = -1, we are in passive
			this.dataChannel.sendASCIIPassive(data);
		} else {
			this.dataChannel.sendASCIIActive(data);
		}

		// the path to display to the FTP client must be "/" instead of the root
		// directory.
		String displayDir = this.store.getCurrentDirectory().replaceFirst(Pattern.quote(this.store.getRootDirectory()),
				"/");
		return new FtpReply(2, 2, 6, "Successfully transferred \"" + displayDir + "\"");
	}

	/**
	 * Lists the directory files and returns the format built with the directory
	 * list. The result of this function is aimed to be returned in the FtpReply for
	 * the client.
	 * 
	 * @return the formatted list of files in the current directory.
	 * @throws IOException if a file system error occured.
	 */
	private String listDirectory() throws IOException {
		Path dirPath = Paths.get(this.store.getCurrentDirectory());
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
		Files.list(dirPath).forEach(path -> {
			File son = new File(path.toAbsolutePath().toString());
			if (son.isDirectory()) {
				sb.append("d");
			} else {
				sb.append("-");
			}
			String perms = "";
			perms += son.canRead() ? "r" : "-";
			perms += son.canWrite() ? "w" : "-";
			perms += son.canExecute() ? "x" : "-";

			// adds 3 times to fake permissions for owners groups and others with the
			// current user's perms
			sb.append(perms);
			sb.append(perms);
			sb.append(perms);

			sb.append(" 1 ftp ftp         ");
			sb.append(son.length());
			sb.append(" ");
			sb.append(dateFormat.format(son.lastModified()));
			sb.append(" ");
			sb.append(son.getName());
			sb.append("\r\n");
		});
		System.out.println(sb.toString());
		return sb.toString();
	}
}
