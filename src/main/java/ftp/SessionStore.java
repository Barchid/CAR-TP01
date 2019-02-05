package ftp;

import java.net.InetSocketAddress;

/**
 * @author Sami BARCHID Represents the state of the client's session.
 */
public class SessionStore {
	public static final int TYPE_ASCII = 1;
	public static final int TYPE_IMAGE = 2;

	private boolean loggedIn;
	private String username;
	private String password;
	private InetSocketAddress activeAdr;
	private int passivePort;
	private int transferType;

	/**
	 * @param loggedIn
	 * @param username
	 * @param password
	 */
	public SessionStore(boolean loggedIn, String username, String password) {
		super();
		this.loggedIn = loggedIn;
		this.username = username;
		this.password = password;
		this.activeAdr = null;
		this.passivePort = -1;
		this.transferType = TYPE_ASCII;
	}

	/**
	 * Constructor used in the beginning of a FTP connection with a client
	 * 
	 */
	public SessionStore() {
		this(false, "", "");
	}

	/**
	 * @return the loggedIn
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @param loggedIn the loggedIn to set
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the activeAdr
	 */
	public InetSocketAddress getActiveAdr() {
		return activeAdr;
	}

	/**
	 * @param activeAdr the activeAdr to set
	 */
	public void setActiveAdr(InetSocketAddress activeAdr) {
		this.activeAdr = activeAdr;
	}

	/**
	 * @return the passivePort
	 */
	public int getPassivePort() {
		return passivePort;
	}

	/**
	 * @param passivePort the passivePort to set
	 */
	public void setPassivePort(int passivePort) {
		this.passivePort = passivePort;
	}

	/**
	 * @return the transferType
	 */
	public int getTransferType() {
		return transferType;
	}

	/**
	 * @param transferType the transferType to set
	 */
	public void setTransferType(int transferType) {
		if (transferType != TYPE_ASCII && transferType != TYPE_IMAGE) {
			return;
		}

		this.transferType = transferType;
	}
}
