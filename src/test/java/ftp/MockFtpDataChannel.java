package ftp;

import java.io.IOException;

/**
 * Mock class used to test the control classes that manages the FTP commands.
 * 
 * @author barchid
 *
 */
public class MockFtpDataChannel extends FtpDataChannel {
	public boolean hasCalledDataMethod = false;

	public MockFtpDataChannel(SessionStore store, FtpControlChannel controlChannel) {
		super(store, controlChannel);
	}

	@Override
	public void sendASCIIActive(String data) throws IOException {
		this.hasCalledDataMethod = true;
	}

	@Override
	public void sendASCIIPassive(String data) throws IOException {
		this.hasCalledDataMethod = true;
	}

	@Override
	public void sendImageActive(byte[] data) throws IOException {
		this.hasCalledDataMethod = true;
	}

	@Override
	public void sendImagePassive(byte[] data) throws IOException {
		this.hasCalledDataMethod = true;
	}

	@Override
	public String readASCIIActive() throws IOException {
		this.hasCalledDataMethod = true;
		return "Coucou";
	}

	@Override
	public String readASCIIPassive() throws IOException {
		this.hasCalledDataMethod = true;
		return "Coucou";
	}

	@Override
	public byte[] readImageActive() throws IOException {
		this.hasCalledDataMethod = true;
		return new byte[] { 1, 2, 3 };
	}

	@Override
	public byte[] readImagePassive() throws IOException {
		this.hasCalledDataMethod = true;
		return new byte[] { 1, 2, 3 };
	}
}
