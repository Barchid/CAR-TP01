package ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpDeleControl;

public class FtpDeleControlTest extends FtpTest {

	public FtpDeleControlTest() throws Exception {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.control = new FtpDeleControl(store);
	}

	@Test
	public void testNotLoggedIn() throws IOException {
		this.store.setLoggedIn(false);
		FtpReply reply = this.executeCommand("PWD", null);
		assertEquals("530", reply.getCode());
		assertEquals("Please log in with USER and PASS first.", reply.getMessage());
	}
}
