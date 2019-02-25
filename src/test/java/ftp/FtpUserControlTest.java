package ftp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpUserControl;

public class FtpUserControlTest extends FtpTest {
	
	public FtpUserControlTest() throws Exception {
		super();
	}
	
	@Before
	public void setUp() throws IOException { 
		Map<String, String> userPasswords = new HashMap<String, String>();
		userPasswords.put("tester", "password");
		this.config.setUserPassword(userPasswords);
		this.control = new FtpUserControl(store);
	}

	@Test
	public void testSuccess() throws IOException {
		FtpReply reply = this.executeCommand("USER", "tester");
		assertEquals("331", reply.getCode());
		assertEquals("Password required for tester", reply.getMessage());
		assertEquals("tester", this.store.getUsername());
	}

	@Test
	public void testArgNull() throws IOException {
		FtpReply reply = this.executeCommand("USER", null);
		assertEquals("501", reply.getCode());
		assertEquals("Syntax error", reply.getMessage());
	}
	
	@Test
	public void testLoginIncorrect() throws IOException {
		FtpReply reply = this.executeCommand("USER", "mdr je ne suis pas connu lol");
		assertEquals("530", reply.getCode());
		assertEquals("Login or password incorrect!", reply.getMessage());
	}
	
	@Test
	public void testAlreadyLoggedIn() throws IOException {
		this.store.setUsername("blabla");
		this.store.setPassword("je suis un mdp");
		this.store.setLoggedIn(true);
		
		FtpReply reply = this.executeCommand("USER", "tester");
		assertEquals("331", reply.getCode());
		assertEquals("Password required for tester", reply.getMessage());
		assertEquals("tester", this.store.getUsername());
		assertEquals("", this.store.getPassword());
		assertEquals(false, this.store.isLoggedIn());
	}
}
