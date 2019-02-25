package ftp;

import java.net.InetSocketAddress;

/**
 * @author Sami BARCHID
 * 
 *         Represents the state of the client's session. This is basically a
 *         POJO injected in all control objects that will be read and modified
 *         by them.
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
	private AppConfig appConfig;
	private String rootDirectory;
	private String currentDirectory;
	private String renameFrom;

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
		this.rootDirectory = null;
		this.currentDirectory = null;
		this.passivePort = -1;
		this.transferType = TYPE_ASCII;
		this.renameFrom = null;
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

	/**
	 * @return the rootDirectory
	 */
	public String getRootDirectory() {
		return rootDirectory;
	}

	/**
	 * @param rootDirectory the rootDirectory to set
	 */
	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	/**
	 * @return the currentDirectory
	 */
	public String getCurrentDirectory() {
		return currentDirectory;
	}

	/**
	 * @param currentDirectory the currentDirectory to set
	 */
	public void setCurrentDirectory(String currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	/**
	 * @return the appConfig
	 */
	public AppConfig getAppConfig() {
		return appConfig;
	}

	/**
	 * @param appConfig the appConfig to set
	 */
	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	/**
	 * @return the renameFrom
	 */
	public String getRenameFrom() {
		return renameFrom;
	}

	/**
	 * @param renameFrom the renameFrom to set
	 */
	public void setRenameFrom(String renameFrom) {
		this.renameFrom = renameFrom;
	}
}
