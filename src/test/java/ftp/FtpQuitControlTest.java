package ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpQuitControl;

public class FtpQuitControlTest extends FtpTest {

	public FtpQuitControlTest() throws Exception {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.control = new FtpQuitControl(store);
	}

	@Test
	public void test() throws IOException {
		FtpReply reply = this.executeCommand("QUIT", null);
		assertEquals("221", reply.getCode());
		assertEquals("Goodbye", reply.getMessage());
	}

}
