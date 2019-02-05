package ftp;

import java.io.IOException;

/**
 * @author Sami BARCHID Represents the data channel of an Ftp communication
 */
public class FtpDataChannel {
	private SessionStore store;
	private FtpControlChannel controlChannel;

	/**
	 * @param store
	 * @param controlChannel
	 */
	public FtpDataChannel(SessionStore store, FtpControlChannel controlChannel) {
		super();
		this.store = store;
		this.controlChannel = controlChannel;
	}

	/**
	 * Sends the data to the client. This method is called by the control objects.
	 * @param data the data to send to the server.
	 */
	public sendData(String data) {
		
	}

	/**
	 * Sends an end of transfer confirmation. This method must be called AFTER the
	 * 
	 * @throws IOException
	 */
	private void sendConfirmationReply() throws IOException {
		this.controlChannel.sendReply(new FtpReply(2, 2, 6, "Successfully transferred."));
	}

	/**
	 * Sends an FTP reply through the FTP control channel to notify the failure of
	 * the data transfer.
	 * 
	 * @throws IOException
	 */
	private void sendFailureReply() throws IOException {
		this.controlChannel.sendReply(new FtpReply(4, 0, 0, "The data transfer could not be sent for the moment..."));
	}
}
