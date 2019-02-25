package ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpSystControl;

public class FtpSystControlTest extends FtpTest {

	public FtpSystControlTest() throws Exception {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.control = new FtpSystControl(store);
	}

	@Test
	public void test() throws IOException {
		FtpReply reply = this.executeCommand("SYST", null);
		assertEquals("215", reply.getCode());
		assertEquals("JVM emulated by Barchid.", reply.getMessage());
	}

}
