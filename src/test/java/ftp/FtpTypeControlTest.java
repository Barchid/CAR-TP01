package ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpTypeControl;

public class FtpTypeControlTest extends FtpTest {

	public FtpTypeControlTest() throws Exception {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.control = new FtpTypeControl(store);
		this.store.setLoggedIn(true);
	}

	@Test
	public void testNotLoggedIn() throws IOException {
		this.store.setLoggedIn(false);
		FtpReply reply = this.executeCommand("TYPE", "blablabliblablou");
		assertEquals("530", reply.getCode());
		assertEquals("Please log in with USER and PASS first.", reply.getMessage());
	}

	@Test
	public void testArgNull() throws IOException {
		FtpReply reply = this.executeCommand("TYPE", null);
		assertEquals("501", reply.getCode());
		assertEquals("Syntax error.", reply.getMessage());
	}
	
	@Test
	public void testASCIIType() throws IOException {
		FtpReply reply = this.executeCommand("TYPE", "A");
		assertEquals("200", reply.getCode());
		assertEquals("Type set to A", reply.getMessage());
		assertEquals(SessionStore.TYPE_ASCII, this.store.getTransferType());
	}
	
	@Test
	public void testImageType() throws IOException {
		FtpReply reply = this.executeCommand("TYPE", "I");
		assertEquals("200", reply.getCode());
		assertEquals("Type set to I", reply.getMessage());
		assertEquals(SessionStore.TYPE_IMAGE, this.store.getTransferType());
	}
}
