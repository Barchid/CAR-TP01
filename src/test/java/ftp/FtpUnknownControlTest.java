package ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpUnknownControl;

public class FtpUnknownControlTest extends FtpTest {

	public FtpUnknownControlTest() throws Exception {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.control = new FtpUnknownControl(store);
	}

	@Test
	public void test() throws IOException {
		FtpReply reply = this.executeCommand("dpdzepoẑerefnk", null);
		assertEquals("500", reply.getCode());
		assertEquals("Syntax error, command unrecognized.", reply.getMessage());
	}

}
