package ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpAuthControl;

/**
 * @author Sami BARCHID
 *
 */
public class FtpAuthControlTest extends FtpTest {
	public FtpAuthControlTest() throws Exception {
		super();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.control = new FtpAuthControl(store);
	}

	/**
	 * Test the only possible case in the handle method of AUTH FTP control.
	 * @throws IOException 
	 */
	@Test
	public void testHandle() throws IOException {
		FtpReply reply = this.executeCommand("AUTH", "blabla");
		assertEquals("502", reply.getCode());
		assertEquals("Explicit TLS authentication not allowed", reply.getMessage());
	}

}
