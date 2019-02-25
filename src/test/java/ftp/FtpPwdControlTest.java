package ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpPwdControl;

public class FtpPwdControlTest extends FtpTest {

	public FtpPwdControlTest() throws Exception {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.control = new FtpPwdControl(store);
	}

	@Test
	public void testNotLoggedIn() throws IOException {
		this.store.setLoggedIn(false);
		FtpReply reply = this.executeCommand("PWD", null);
		assertEquals("530", reply.getCode());
		assertEquals("Please log in with USER and PASS first.", reply.getMessage());
	}

	@Test
	public void testWorking() throws IOException {
		this.store.setLoggedIn(true);
		this.store.setRootDirectory("/test/test");
		this.store.setCurrentDirectory("/test/test/lol/");
		FtpReply reply = this.executeCommand("PWD", null);
		assertEquals("257", reply.getCode());
		assertEquals("\"//lol/\" is current directory.", reply.getMessage());
	}
}
