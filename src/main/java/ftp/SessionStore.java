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

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public InetSocketAddress getActiveAdr() {
		return activeAdr;
	}

	/**
	 * @param activeAdr the activeAdr to set
	 */
	public void setActiveAdr(InetSocketAddress activeAdr) {
		this.activeAdr = activeAdr;
	}

	public int getPassivePort() {
		return passivePort;
	}

	public void setPassivePort(int passivePort) {
		this.passivePort = passivePort;
	}

	public int getTransferType() {
		return transferType;
	}

	public void setTransferType(int transferType) {
		if (transferType != TYPE_ASCII && transferType != TYPE_IMAGE) {
			return;
		}

		this.transferType = transferType;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public String getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(String currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	public String getRenameFrom() {
		return renameFrom;
	}

	public void setRenameFrom(String renameFrom) {
		this.renameFrom = renameFrom;
	}
}
