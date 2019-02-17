package ftp;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.junit.Assert;

/**
 * @author Sami BARCHID
 *
 */
public abstract class FtpTest {
	private Socket client;

	public FtpTest() {
	}

	/**
	 * Writes a command to the
	 * 
	 * @param command
	 */
	public void writeCommand(String command) {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()))) {
			command += "\r\n";
			writer.write(command);
			writer.flush();
		} catch (Exception ex) {
			Assert.fail("Cannot send command");
		}
	}

	public String read() {
		try (BufferedInputStream reader = new BufferedInputStream(this.client.getInputStream())) {
			String response = "";
			int stream;
			byte[] b = new byte[4096];
			stream = reader.read(b);
			response = new String(b, 0, stream);

			return response;
		} catch (Exception ex) {
			Assert.fail("Could not read command.");
			return null; // not reachable
		}
	}
}
