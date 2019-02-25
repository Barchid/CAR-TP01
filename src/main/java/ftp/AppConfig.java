package ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Sami BARCHID
 * 
 *         Represents the configurations of the application. This class will
 *         load the "config.properties" to load the configurations.
 */
public class AppConfig {
	private int portNumber;
	private Map<String, String> userDirectories = new HashMap<String, String>();
	private Map<String, String> userPassword = new HashMap<String, String>();

	/**
	 * Loads the configurations properties with the config.properties file located
	 * in the project racine.
	 * 
	 * 
	 * @throws Exception if the config.properties file cannot be read.
	 */
	public AppConfig() throws Exception {
		System.out.println("Loading the properties file...");
		try (InputStream file = new FileInputStream("config.properties")) {
			Properties properties = new Properties();
			properties.load(file);

			String port = properties.getProperty("port");
			if (port == null) {
				throw new Exception("Properties format error : Port number is undefined in config.properties.");
			}

			String passwordsString = properties.getProperty("passwords");
			if (passwordsString == null) {
				throw new Exception("Properties format error : Passwords are undefined in config.properties.");
			}

			String directoriesString = properties.getProperty("directories");
			if (directoriesString == null) {
				throw new Exception("Properties format error : Directories are undefined in config.properties.");
			}

			String usersString = properties.getProperty("users");
			if (usersString == null) {
				throw new Exception("Properties format error : Users are undefined in config.properties.");
			}

			String[] users = usersString.split(",");
			String[] directories = directoriesString.split(",");
			String[] passwords = passwordsString.split(",");

			if (users.length != directories.length || users.length != passwords.length) {
				throw new Exception(
						"Properties format error : Wrong length of passwords or directories compared to users number.");
			}

			for (int i = 0; i < users.length; i++) {
				File directory = new File(directories[i]);
				this.userDirectories.put(users[i], directory.getAbsolutePath());
				this.userPassword.put(users[i], passwords[i]);
			}

			this.portNumber = Integer.parseInt(port);
		} catch (IOException ex) {
			throw new Exception("Error while reading the properties file. Cannot launch the server.");
		}
	}

	public int getPortNumber() {
		return portNumber;
	}

	public Map<String, String> getUserDirectories() {
		return userDirectories;
	}

	public Map<String, String> getUserPassword() {
		return userPassword;
	}

	public void setUserDirectories(Map<String, String> userDirectories) {
		this.userDirectories = userDirectories;
	}

	public void setUserPassword(Map<String, String> userPassword) {
		this.userPassword = userPassword;
	}
}
