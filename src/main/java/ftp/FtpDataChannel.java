package ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * @author Sami BARCHID Represents the data channel of an Ftp communication
 */
public class FtpDataChannel {
	private SessionStore store;
	private FtpControlChannel controlChannel;

	/**
	 * @param store
	 * @param controlChannel
	 */
	public FtpDataChannel(SessionStore store, FtpControlChannel controlChannel) {
		super();
		this.store = store;
		this.controlChannel = controlChannel;
	}

	public void sendASCIIActive(String data) throws IOException {
		this.sendOpeningReply();
		try (Socket socket = new Socket(this.store.getActiveAdr().getAddress(), this.store.getActiveAdr().getPort());) {
			System.out.println("Writing ASCII data through data channel.");
			this.writeASCIIData(data, socket);
		} catch (IOException exception) {
			System.err.println("Could not open connection data. Send error to the control channel.");
			this.sendFailureReply();
		}
	}

	public void sendASCIIPassive(String data) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(this.store.getPassivePort())) {
			serverSocket.setSoTimeout(10000); // set the timeout to 10 seconds.
			System.out.println("Waiting for client passive data channel...");
			Socket socket = serverSocket.accept();
			this.sendOpeningReply();

			System.out.println("Writing data through data channel.");
			this.writeASCIIData(data, socket);
		} catch (SocketTimeoutException e) {
			System.out.println("Timeout of the socket reached. Send error in the control channel.");
			this.sendTimeoutReply();
		} catch (IOException e) {
			System.err.println("Could not open data connection. Send error in the control channel.");
			this.sendFailureReply();
		}
	}

	/**
	 * Transfers the data to the client. This method is called by the control
	 * objects.
	 * 
	 * @param data the data to send to the server.
	 * @throws IOException
	 */
	public void sendImageActive(byte[] data) throws IOException {
		try (Socket socket = new Socket(this.store.getActiveAdr().getAddress(), this.store.getActiveAdr().getPort())) {
			System.out.println("Writing IMAGE data through data channel.");
			this.writeImageData(data, socket);

			this.sendOpeningReply();
		} catch (IOException exception) {
			System.err.println("Could not open connection data. Send error to the control channel.");
			this.sendFailureReply();
		}
	}

	public void sendImagePassive(byte[] data) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(this.store.getPassivePort())) {
			serverSocket.setSoTimeout(10000); // set the timeout to 10 seconds.
			System.out.println("Waiting for client passive data channel...");
			Socket socket = serverSocket.accept();

			System.out.println("Writing data through data channel.");
			this.writeImageData(data, socket);

			this.sendOpeningReply();
		} catch (SocketTimeoutException e) {
			System.out.println("Timeout of the socket reached. Send error in the control channel.");
			this.sendTimeoutReply();
		} catch (IOException e) {
			System.err.println("Could not open data connection. Send error in the control channel.");
			this.sendFailureReply();
		}
	}

	public String readASCIIActive() throws IOException {
		try (Socket socket = new Socket(this.store.getActiveAdr().getAddress(), this.store.getActiveAdr().getPort());) {
			this.sendOpeningReply();
			System.out.println("Receiving ASCII data through data channel.");
			String data = this.readASCIIData(socket);
			return data;
		} catch (IOException exception) {
			System.err.println("Could not open connection data. Send error to the control channel.");
			this.sendFailureReply();
			return null;
		}
	}

	public String readASCIIPassive() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(this.store.getPassivePort())) {
			serverSocket.setSoTimeout(10000); // set the timeout to 10 seconds.
			System.out.println("Waiting for client passive data channel...");
			Socket socket = serverSocket.accept();
			this.sendOpeningReply();

			System.out.println("Reading data through data channel.");
			String data = this.readASCIIData(socket);
			System.out.println("ASCII data received.");
			return data;
		} catch (SocketTimeoutException e) {
			System.out.println("Timeout of the socket reached. Send error in the control channel.");
			this.sendTimeoutReply();
			return null;
		} catch (IOException e) {
			System.err.println("Could not open data connection. Send error in the control channel.");
			this.sendFailureReply();
			return null;
		}
	}

	public byte[] readImageActive() throws IOException {
		try (Socket socket = new Socket(this.store.getActiveAdr().getAddress(), this.store.getActiveAdr().getPort());) {
			this.sendOpeningReply();
			System.out.println("Receiving ASCII data through data channel.");
			byte[] data = this.readImageData(socket);
			System.out.println("Image data received.");
			return data;
		} catch (IOException exception) {
			System.err.println("Could not open connection data. Send error to the control channel.");
			this.sendFailureReply();
			return null;
		}
	}

	public byte[] readImagePassive() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(this.store.getPassivePort())) {
			serverSocket.setSoTimeout(10000); // set the timeout to 10 seconds.
			System.out.println("Waiting for client passive data channel...");
			Socket socket = serverSocket.accept();
			this.sendOpeningReply();

			System.out.println("Reading data through data channel.");
			byte[] data = this.readImageData(socket);
			System.out.println("ASCII data received.");
			return data;
		} catch (SocketTimeoutException e) {
			System.out.println("Timeout of the socket reached. Send error in the control channel.");
			this.sendTimeoutReply();
			return null;
		} catch (IOException e) {
			System.err.println("Could not open data connection. Send error in the control channel.");
			this.sendFailureReply();
			return null;
		}
	}

	/**
	 * Sends the data in ASCII type to the client through the data channel.
	 * 
	 * @param asciiData the ASCII data to send to the client.
	 * @throws IOException when there is a problem in the communication
	 */
	private void writeASCIIData(String asciiData, Socket socket) throws IOException {
		try (BufferedWriter dataOut = new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
			dataOut.write(asciiData + "\r\n");
			dataOut.flush();
		}
	}

	/**
	 * 
	 * @param imageData
	 * @param socket
	 * @throws IOException
	 */
	private void writeImageData(byte[] imageData, Socket socket) throws IOException {
		try (BufferedOutputStream dataOut = new BufferedOutputStream(socket.getOutputStream())) {
			dataOut.write(imageData);
		}
	}

	/**
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private String readASCIIData(Socket socket) throws IOException {
		try (BufferedReader dataIn = new BufferedReader(
				new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder sb = new StringBuilder();
			for (String line = dataIn.readLine(); line != null; line = dataIn.readLine()) {
				sb.append(line);
			}
			return sb.toString();
		}
	}

	/**
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private byte[] readImageData(Socket socket) throws IOException {
		try (BufferedInputStream dataIn = new BufferedInputStream(socket.getInputStream())) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read = 0;
			while ((read = dataIn.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, read);
			}
			baos.flush();
			return baos.toByteArray();
		}
	}

	/**
	 * Sends an end of transfer confirmation. This method must be called AFTER the
	 * 
	 * @throws IOException when an unknown error occurs
	 */
	private void sendOpeningReply() throws IOException {
		this.controlChannel.sendReply(new FtpReply(1, 5, 0, "Opening data channel."));
	}

	/**
	 * Sends an FTP reply through the FTP control channel to notify the failure of
	 * the data transfer.
	 * 
	 * @throws IOException when an unknown error occurs
	 */
	private void sendFailureReply() throws IOException {
		this.controlChannel.sendReply(new FtpReply(4, 0, 0, "The data transfer could not be sent for the moment..."));
	}

	/**
	 * Sends a timeout error reply to the client through the control channel.
	 * 
	 * @throws IOException when an unknown error occurs
	 */
	private void sendTimeoutReply() throws IOException {
		this.controlChannel.sendReply(new FtpReply(4, 2, 5, "Time out reached"));
	}
}
