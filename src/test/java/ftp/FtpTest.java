package ftp;

import java.io.IOException;

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
}
