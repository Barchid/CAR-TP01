package ftp;

/**
 * @author Sami BARCHID
 *
 */
public abstract class FtpTest {
	protected SessionStore store;

	public FtpTest() {
		this.store = new SessionStore();
	}
}
