package ftp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.unix4j.Unix4j;
import org.unix4j.unix.Ls;

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
			send("PWD", writer);
			System.out.println(read(reader));

			send("TYPE I", writer);
			System.out.println(read(reader));

			send("LIST", writer);
			String ls = read(reader);
			System.out.println(ls);

			ServerSocket dataChannel = new ServerSocket(45 * 256 + 21);
			Socket dataTransfer = dataChannel.accept();
			BufferedInputStream dataReader = new BufferedInputStream(dataTransfer.getInputStream());
			String data = read(dataReader);
			System.out.println(data);
			System.out.println(read(reader));

			send("CWD /SQL/sql_exercices.sql", writer);
			System.out.println(read(reader));

			File dir = new File("sami/rdf-semaine-1-moments");
			for (File son : dir.listFiles()) {
				StringBuilder sb = new StringBuilder();
				if (son.isDirectory()) {
					sb.append("d");
				} else {
					sb.append("-");
				}
				String perms = "";
				perms += son.canRead() ? "r" : "-";
				perms += son.canWrite() ? "w" : "-";
				perms += son.canExecute() ? "x" : "-";

				sb.append(perms);
				sb.append(perms);
				sb.append(perms);

				sb.append(" 1 ftp ftp         ");
				sb.append(son.length());
				sb.append(" ");
				SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
				sb.append(format.format(son.lastModified()));
				sb.append(" ");
				sb.append(son.getName());
				sb.append("\r\n");
				System.out.print(sb.toString());
			}
			String lol = Unix4j.ls(Ls.Options.l.h, dir).toStringResult();
			System.out.println(lol);
		} catch (Exception e) {
			e.printStackTrace();
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
