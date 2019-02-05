package ftp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Sami BARCHID
 *
 */
public class Tester {

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		try (Socket socket = new Socket("localhost", 21);
				BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));) {
			System.out.println(read(reader)); // read welcome message
			send("USER barchid", writer);
			System.out.println(read(reader));
			send("PASS lol", writer);
			System.out.println(read(reader));
			send("PASV", writer);
			System.out.println(read(reader));
			send("PORT 127,0,0,1,45,21", writer);
			System.out.println(read(reader));
			send("LIST", writer);
			System.out.println(read(reader));
			send("LIST", writer);
			System.out.println(read(reader));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private static String read(BufferedInputStream reader) throws IOException {
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);

		return response;
	}

	private static void send(String command, BufferedWriter writer) throws IOException {
		command += "\r\n";
		writer.write(command);
		writer.flush();
	}
}
