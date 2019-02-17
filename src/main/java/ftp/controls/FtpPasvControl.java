package ftp.controls;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public class FtpPasvControl extends FtpControl {

	/**
	 * @param store
	 */
	public FtpPasvControl(SessionStore store) {
		super(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ftp.controls.FtpControl#handle(ftp.FtpCommand)
	 */
	@Override
	public FtpReply handle(FtpCommand command) {
		if (!this.store.isLoggedIn()) {
			return new FtpReply(5, 3, 0, "Please log in with USER and PASS first.");
		}
		int port = this.createPortValidAndAvailable();
		if (port == -1) {
			return new FtpReply(4, 2, 5, "Cannot establish data connection.");
		}

		String param = this.createAdr(port);
		if (param == null) {
			return new FtpReply(4, 2, 5, "Cannot establish data connection.");
		}

		this.store.setLoggedIn(true);
		this.store.setPassivePort(port);
		return new FtpReply(2, 0, 0, "Entering Passive Mode " + param);
	}

	private int createPortValidAndAvailable() {
		for (int i = 1024; i <= 65535; i++) {
			// IF [i can use this port]
			if (this.isPortValidAndAvailable(i)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Checks to see if a specific port is valid and available. This implementation
	 * is inspired by the Apache Camel project.
	 *
	 * @param port the port to check for availability
	 * @return true if the port is valid and available or else false
	 */
	private boolean isPortValidAndAvailable(int port) {
		if (port < 1024 || port > 65535) {
			return false;
		}

		try (ServerSocket ss = new ServerSocket(port)) {
			ss.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Creates the address params of the PASV command's reply.
	 * 
	 * @param port the port number to use for the passive data channel
	 * @return the address params to use for the reply or null if there is a problem
	 */
	private String createAdr(int port) {
		try {
			String ip = Inet4Address.getLocalHost().getHostAddress();
			String[] nums = ip.split("\\.");
			int port1 = port / 256;
			int port2 = port - port1;
			return "(" + nums[0] + "," + nums[1] + "," + nums[2] + "," + nums[3] + "," + port1 + "," + port2 + ")";
		} catch (UnknownHostException e) {
			return null;
		}
	}
}
