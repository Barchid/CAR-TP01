package ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpPortControl;

public class FtpPortControlTest extends FtpTest {

	public FtpPortControlTest() throws Exception {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.control = new FtpPortControl(store);
	}

	@Test
	public void testNotLoggedIn() throws IOException {
		this.store.setLoggedIn(false);
		FtpReply reply = this.executeCommand("PORT", null);
		assertEquals("530", reply.getCode());
		assertEquals("Please log in with USER and PASS first.", reply.getMessage());
	}

	@Test
	public void testArgNull() throws IOException {
		this.store.setLoggedIn(true);
		FtpReply reply = this.executeCommand("PORT", null);
		assertEquals("501", reply.getCode());
		assertEquals("Syntax error", reply.getMessage());
	}

	@Test
	public void testArgNotValid() throws IOException {
		this.store.setLoggedIn(true);
		FtpReply reply = this.executeCommand("PORT", "Je suis un argument très très nul");
		assertEquals("501", reply.getCode());
		assertEquals("Syntax error : port number not valid.", reply.getMessage());
	}

	@Test
	public void testWorking() throws IOException {
		this.store.setLoggedIn(true);
		FtpReply reply = this.executeCommand("PORT", "127,0,0,1,224,149");
		assertEquals("200", reply.getCode());
		assertEquals("Port command successful", reply.getMessage());
		assertEquals(224*256 + 149, this.store.getActiveAdr().getPort());
	}
}
