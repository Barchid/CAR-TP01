/**
 * @author Sami BARCHID
 */
package ftp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
	public static void main(String[] args) throws Exception {
		AppConfig appConfig = new AppConfig();

		System.out.println("Initializing server on port : " + appConfig.getPortNumber() + "...");
		try (ServerSocket server = new ServerSocket(appConfig.getPortNumber())) {
			System.out.println("Creating thread pool...");
			// Use threadpool with a number of the available processors in the runtime.
			ExecutorService executor = Executors.newFixedThreadPool(10);

			System.out.println("Waiting for clients...");
			while (true) {
				Socket client = server.accept();
				System.out.println("Client connection received from " + client.getInetAddress().toString());
				Runnable worker = new FtpCommunication(client, appConfig);
				executor.execute(worker);
			}
		} catch (IOException e) {
			System.err.println("Cannot start FTP server : port number already used.");
		}
	}
}
