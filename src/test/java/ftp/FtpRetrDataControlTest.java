package ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpRetrDataControl;
import ftp.controls.FtpStorDataControl;

public class FtpRetrDataControlTest extends FtpTest {

	public FtpRetrDataControlTest() throws Exception {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.control = new FtpRetrDataControl(store, new MockFtpDataChannel(store, null));
	}

	@Test
	public void testNotLoggedIn() throws IOException {
		this.store.setLoggedIn(false);
		FtpReply reply = this.executeCommand("PWD", null);
		assertEquals("530", reply.getCode());
		assertEquals("Please log in with USER and PASS first.", reply.getMessage());
	}
}
