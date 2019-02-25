package ftp.controls;

import java.util.HashMap;
import java.util.Map;

import ftp.FtpDataChannel;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 * 
 *         Factory pattern used to create every controller instance of the
 *         available FTP commands in the server.
 *
 */
public enum FtpControlFactory {
	INSTANCE;

	/**
	 * Creates every instances of the FTP controllers for the available FTP commands
	 * in the server and pack them into a Map object
	 * 
	 * @param store       the session store singleton that is shared between every
	 *                    instances of controller.
	 * @param dataChannel the data channel singleton used by the controllers that
	 *                    need to create a data connection with the client.
	 * @return the map of all the commands code linked by their controller instance.
	 */
	public Map<String, FtpControl> getControlsMap(SessionStore store, FtpDataChannel dataChannel) {
		Map<String, FtpControl> controls = new HashMap<String, FtpControl>();

		FtpPwdControl pwd = new FtpPwdControl(store);
		controls.put("PWD", pwd);
		controls.put("XPWD", pwd);
		controls.put("SYST", new FtpSystControl(store));
		controls.put("AUTH", new FtpAuthControl(store));
		controls.put("USER", new FtpUserControl(store));
		controls.put("PASS", new FtpPassControl(store));
		controls.put("QUIT", new FtpQuitControl(store));
		controls.put("Unknown", new FtpUnknownControl(store));
		controls.put("LIST", new FtpListDataControl(store, dataChannel));
		controls.put("MLSD", new FtpListDataControl(store, dataChannel));
		controls.put("TYPE", new FtpTypeControl(store));
		controls.put("PASV", new FtpPasvControl(store));
		controls.put("PORT", new FtpPortControl(store));
		controls.put("PWD", new FtpPwdControl(store));
		controls.put("CDUP", new FtpCdupControl(store));
		controls.put("CWD", new FtpCwdControl(store));
		controls.put("RETR", new FtpRetrDataControl(store, dataChannel));
		controls.put("RNFR", new FtpRnfrControl(store));
		controls.put("RNTO", new FtpRntoControl(store));
		controls.put("DELE", new FtpDeleControl(store));
		controls.put("RMD", new FtpRmdControl(store));
		controls.put("MKD", new FtpMkdControl(store));
		controls.put("STOR", new FtpStorDataControl(store, dataChannel));
		return controls;
	}
}
