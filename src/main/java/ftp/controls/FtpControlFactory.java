package ftp.controls;

import java.util.HashMap;
import java.util.Map;

import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 */
public enum FtpControlFactory {
	INSTANCE;

	public Map<String, FtpControl> getControlsMap(SessionStore store) {
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

		return controls;
	}
}
