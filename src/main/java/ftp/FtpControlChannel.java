package ftp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author Sami BARCHID Represents the control channel of an FTP communication.
 */
public class FtpControlChannel {
	private BufferedWriter writer;
	private BufferedReader reader;

	/**
	 * @param writer
	 * @param reader
	 */
	public FtpControlChannel(BufferedWriter writer, BufferedReader reader) {
		super();
		this.writer = writer;
		this.reader = reader;
	}

	/**
	 * Sends an FTP reply to the client through the control channel.
	 * 
	 * @param reply the FTP reply to send
	 * @throws IOException
	 */
	public void sendReply(FtpReply reply) throws IOException {
		System.out.println("> " + reply.getCode() + " " + reply.getMessage());
		this.writer.write(reply.toString());
		this.writer.flush();
	}

	public FtpCommand readCommand() throws IOException {
		String line = this.reader.readLine();
		if (line == null) {
			return null;
		}

		String[] words = line.split(" ");
		if (words.length > 1) {
			System.out.println("Command received : " + words[0] + " " + words[1]);
			return new FtpCommand(words[0], words[1]);
		} else {
			System.out.println("Command received : " + words[0]);
			return new FtpCommand(words[0]);
		}
	}

	/**
	 * @return the writer
	 */
	public BufferedWriter getWriter() {
		return writer;
	}

	/**
	 * @return the reader
	 */
	public BufferedReader getReader() {
		return reader;
	}
}
