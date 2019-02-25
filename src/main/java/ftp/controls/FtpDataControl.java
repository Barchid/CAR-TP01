package ftp.controls;

import ftp.FtpDataChannel;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 *         Abstract class used to encapsulate the logic behind the control
 *         command that can use a data channel to exchange data with the client
 */
public abstract class FtpDataControl extends FtpControl {
	protected FtpDataChannel dataChannel;

	/**
	 * @param store       the store of the client's connection
	 * @param dataChannel the service that manage the data channel connection with
	 *                    the client.
	 */
	public FtpDataControl(SessionStore store, FtpDataChannel dataChannel) {
		super(store);
		this.dataChannel = dataChannel;
	}
}
