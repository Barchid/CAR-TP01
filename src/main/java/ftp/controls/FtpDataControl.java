package ftp.controls;

import ftp.FtpCommand;
import ftp.FtpDataChannel;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public abstract class FtpDataControl extends FtpControl {
	protected FtpDataChannel dataChannel;

	/**
	 * @param store
	 */
	public FtpDataControl(SessionStore store, FtpDataChannel dataChannel) {
		super(store);
		this.dataChannel = dataChannel;
	}
}
