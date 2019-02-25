package ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpPasvControl;

public class FtpPasvControlTest extends FtpTest {

	public FtpPasvControlTest() throws Exception {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.control = new FtpPasvControl(store);
	}

	@Test
	public void testNotLoggedIn() throws IOException {
		this.store.setLoggedIn(false);
		FtpReply reply = this.executeCommand("PASV", null);
		assertEquals("530", reply.getCode());
		assertEquals("Please log in with USER and PASS first.", reply.getMessage());
	}
	
	@Test
	public void testWorking() throws IOException {
		this.store.setLoggedIn(true);
		FtpReply reply = this.executeCommand("PASV", null);
		assertEquals("200", reply.getCode());
		assertTrue(reply.getMessage().startsWith("Entering Passive Mode"));
		assertNotEquals(-1, this.store.getPassivePort());
	}
}
