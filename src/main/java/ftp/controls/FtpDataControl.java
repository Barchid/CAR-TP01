package ftp.controls;

import ftp.FtpCommand;
import ftp.FtpDataChannel;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public class FtpDataControl extends FtpControl {
	private FtpDataChannel dataChannel;

	/**
	 * @param store
	 */
	public FtpDataControl(SessionStore store, FtpDataChannel dataChannel) {
		super(store);
		this.dataChannel = dataChannel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ftp.controls.FtpControl#handle(ftp.FtpCommand)
	 */
	@Override
	public FtpReply handle(FtpCommand command) {

		return null;
	}

}
