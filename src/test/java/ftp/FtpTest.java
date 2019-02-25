package ftp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import ftp.controls.FtpControl;

/**
 * @author Sami BARCHID
 *
 */
public abstract class FtpTest {
	protected SessionStore store;
	protected AppConfig config;
	protected FtpControl control;

	public FtpTest() throws Exception {
		this.config = new AppConfig();
		this.store = new SessionStore();
		this.store.setAppConfig(config);
	}

	protected FtpReply executeCommand(String commandStr, String arg) throws IOException {
		FtpCommand command = new FtpCommand(commandStr, arg);
		FtpReply reply = this.control.handle(command);
		return reply;
	}

	protected void loadTestDirectory() throws IOException {
		Map<String, String> testMap = new HashMap<>();
		testMap.put("tester", "tester");
		this.config.setUserDirectories(testMap);
		this.config.setUserPassword(testMap);

		Files.createDirectory(Paths.get("tester"));
		Files.createDirectory(Paths.get("tester", "testDirectory"));
		Files.createFile(Paths.get("tester", "testFile"));
	}

	protected void removeDirectories() throws IOException {
		Files.deleteIfExists(Paths.get("tester"));
		Files.deleteIfExists(Paths.get("tester", "testDirectory"));
		Files.deleteIfExists(Paths.get("tester", "testFile"));
	}
}
