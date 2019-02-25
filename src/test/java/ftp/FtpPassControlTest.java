package ftp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ftp.controls.FtpPassControl;

public class FtpPassControlTest extends FtpTest {

	public FtpPassControlTest() throws Exception {
		super();
		Map<String, String> userDirectories = new HashMap<>();
		Map<String, String> userPasswords = new HashMap<>();
		userDirectories.put("tester", "tester");
		userPasswords.put("tester", "tester");
		this.config.setUserPassword(userPasswords);
		this.config.setUserDirectories(userDirectories);
	}

	@Before
	public void setUp() throws Exception {
		this.control = new FtpPassControl(store);	
	}

	@Test
	public void testOk() throws IOException {
		this.store.setUsername("tester");
		this.store.setLoggedIn(false);
		
		FtpReply reply = this.executeCommand("PASS", "tester");
		
		assertEquals("230", reply.getCode());
		assertEquals("Logged on", reply.getMessage());
		assertEquals("tester", this.store.getUsername());
		assertEquals("tester", this.store.getPassword());
		assertEquals("tester", this.store.getCurrentDirectory());
		assertEquals("tester", this.store.getRootDirectory());
	}
	
	@Test
	public void testAlreadyLoggedIn() throws IOException {
		this.store.setLoggedIn(true);
		
		FtpReply reply = this.executeCommand("PASS", "tester");
		
		assertEquals("503", reply.getCode());
		assertEquals("Bad sequence of commands.", reply.getMessage());
	}

	@Test
	public void testUsernameEmpty() throws IOException {
		this.store.setLoggedIn(false);
		this.store.setUsername("");
		
		FtpReply reply = this.executeCommand("PASS", "tester");
		
		assertEquals("530", reply.getCode());
		assertEquals("Login or password incorrect!", reply.getMessage());
	}
	
	@Test
	public void testWrongPassword() throws IOException {
		this.store.setLoggedIn(false);
		this.store.setUsername("tester");
		
		FtpReply reply = this.executeCommand("PASS", "coucou je m'appelle sami");
		
		assertEquals("530", reply.getCode());
		assertEquals("Login or password incorrect!", reply.getMessage());
	}
	
	@Test
	public void testArgEmpty() throws IOException {
		this.store.setLoggedIn(false);
		this.store.setUsername("tester");
		
		FtpReply reply = this.executeCommand("PASS", null);
		
		assertEquals("530", reply.getCode());
		assertEquals("Login or password incorrect!", reply.getMessage());
	}
}
