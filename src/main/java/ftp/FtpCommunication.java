package ftp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import ftp.controls.FtpControl;
import ftp.controls.FtpControlFactory;

/**
 * @author Sami BARCHID
 *
 */
public class FtpCommunication implements Runnable {
	private Socket client;
	private Map<String, FtpControl> controls;
	private AppConfig appConfig;

	/**
	 * @param client
	 */
	public FtpCommunication(Socket client, AppConfig appConfig) {
		super();
		this.client = client;
		this.appConfig = appConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try (BufferedWriter controlOut = new BufferedWriter(
				new OutputStreamWriter(this.client.getOutputStream(), StandardCharsets.UTF_8));
				BufferedReader controlIn = new BufferedReader(
						new InputStreamReader(this.client.getInputStream(), StandardCharsets.UTF_8));) {

			FtpControlChannel controlChannel = new FtpControlChannel(controlOut, controlIn);
			this.initControls(controlChannel);

			System.out.println("Sending welcome message>");
			this.sendWelcomeMessage(controlChannel);

			boolean isRunning = true;
			while (isRunning) {
				// Waits for a command
				FtpCommand command = controlChannel.readCommand();

				// if nothing read in the control channel, then the communication is ended
				if (command == null) {
					System.out.println("Communication ended by the client. Connection abort.");
					isRunning = false;
					continue;
				}
				FtpReply reply = this.executeCommand(command);
				if (reply == null) {
					continue;
				}
				controlChannel.sendReply(reply);

				if (command.getMessage().equals("QUIT")) { // quit command finishes the loop
					isRunning = false;
				}
			}

			// closing the client.
			this.client.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			System.out.println("Error while receiving command/sending reply. Connection abort.");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			System.out.println("Unknown error. Connection abort.");
		}
	}

	/**
	 * Initializes the dictionary of all the known FTP command controllers of the
	 * server.
	 * 
	 * @param controlChannel the ftp control channel (used with the data channel
	 *                       constructor)
	 */
	private void initControls(FtpControlChannel controlChannel) {
		SessionStore store = new SessionStore();
		store.setAppConfig(this.appConfig);
		FtpDataChannel dataChannel = new FtpDataChannel(store, controlChannel);
		this.controls = FtpControlFactory.INSTANCE.getControlsMap(store, dataChannel);
	}

	/**
	 * Executes the specified FTP command with the right controller and returns the
	 * FTP reply that is the result from the controller's handling.
	 * 
	 * @param command the FTP commmand to process.
	 * @throws IOException when a reply could not be sent.
	 * @return FtpReply the FTP reply that results from the controller's handling.
	 */
	private FtpReply executeCommand(FtpCommand command) throws IOException {
		FtpControl control = this.controls.get(command.getMessage());
		if (control == null) { // No controller available for the specified FTP command
			return this.controls.get("Unknown").handle(command);
		} else {
			return control.handle(command);
		}
	}

	private void sendWelcomeMessage(FtpControlChannel controlChannel) throws IOException {
		String welcome = "Wesh alors !";
		controlChannel.sendReply(new FtpReply(2, 2, 0, welcome));
	}
}
