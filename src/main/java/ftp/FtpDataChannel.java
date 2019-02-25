package ftp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * @author Sami BARCHID
 * 
 *         Represents the data channel of an Ftp communication
 */
public class FtpDataChannel {
	private SessionStore store;
	private FtpControlChannel controlChannel;

	public FtpDataChannel(SessionStore store, FtpControlChannel controlChannel) {
		super();
		this.store = store;
		this.controlChannel = controlChannel;
	}

	/**
	 * Sends the ascii data through a new data channel with active FTP mode for
	 * transfer.
	 * 
	 * @param data the ASCII data to send.
	 * @throws IOException if a network error occured on write through socket
	 */
	public void sendASCIIActive(String data) throws IOException {
		this.sendOpeningReply();
		try (Socket socket = new Socket(this.store.getActiveAdr().getAddress(), this.store.getActiveAdr().getPort());) {
			System.out.println("Writing ASCII data through data channel with ASCII active mode.");
//			this.writeASCIIData(data, socket);
			this.writeImageData(data.getBytes(), socket);
		} catch (IOException exception) {
			System.err.println("Could not open connection data. Send error to the control channel.");
			this.sendFailureReply();
		}
	}

	/**
	 * Sends the ASCII data through a new data channel with passive FTP mode for
	 * transfer.
	 * 
	 * @param data the ASCII data to send.
	 * @throws IOException if a network error occured on write through socket.
	 */
	public void sendASCIIPassive(String data) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(this.store.getPassivePort())) {
			serverSocket.setSoTimeout(10000); // set the timeout to 10 seconds.
			System.out.println("Waiting for client passive data channel...");
			Socket socket = serverSocket.accept();
			this.sendOpeningReply();

			System.out.println("Writing data through data channel with ASCII passive mode.");
			this.writeASCIIData(data, socket);
			socket.close();
//			this.writeImageData(data.getBytes(), socket);
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
	 * @throws IOException if a network error occured.
	 */
	public void sendImageActive(byte[] data) throws IOException {
		try (Socket socket = new Socket(this.store.getActiveAdr().getAddress(), this.store.getActiveAdr().getPort())) {
			System.out.println("Writing IMAGE data through data channel with Image active mode.");
			this.writeImageData(data, socket);

			this.sendOpeningReply();
		} catch (IOException exception) {
			System.err.println("Could not open connection data. Send error to the control channel.");
			this.sendFailureReply();
		}
	}

	/**
	 * Sends the image data through a new data channel with the passive FTP mode.
	 * 
	 * @param data the image data to send.
	 * @throws IOException if a network error occured when using the socket.
	 */
	public void sendImagePassive(byte[] data) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(this.store.getPassivePort())) {
			serverSocket.setSoTimeout(10000); // set the timeout to 10 seconds.
			System.out.println("Waiting for client passive data channel...");
			Socket socket = serverSocket.accept();

			System.out.println("Writing data through data channel with Image passive mode.");
			this.writeImageData(data, socket);
			socket.close();
			this.sendOpeningReply();
		} catch (SocketTimeoutException e) {
			System.out.println("Timeout of the socket reached. Send error in the control channel.");
			this.sendTimeoutReply();
		} catch (IOException e) {
			System.err.println("Could not open data connection. Send error in the control channel.");
			this.sendFailureReply();
		}
	}

	/**
	 * Reads the ASCII data through a new data channel with active mode.
	 * 
	 * @return the ASCII data sent by the FTP client.
	 * @throws IOException if a network error occured.
	 */
	public String readASCIIActive() throws IOException {
		try (Socket socket = new Socket(this.store.getActiveAdr().getAddress(), this.store.getActiveAdr().getPort());) {
			this.sendOpeningReply();
			System.out.println("Receiving ASCII data through data channel with ASCII active mode.");
			String data = this.readASCIIData(socket);
			System.out.println(data);
			return data;
		} catch (IOException exception) {
			System.err.println("Could not open connection data. Send error to the control channel.");
			this.sendFailureReply();
			return null;
		}
	}

	/**
	 * Reads the ASCII data through a new data channel with passive mode.
	 * 
	 * @return the data sent by the FTP client.
	 * @throws IOException if a network error occured.
	 */
	public String readASCIIPassive() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(this.store.getPassivePort())) {
			serverSocket.setSoTimeout(10000); // set the timeout to 10 seconds.
			System.out.println("Waiting for client passive data channel...");
			Socket socket = serverSocket.accept();
			this.sendOpeningReply();

			System.out.println("Reading data through data channel with ASII passive mode.");
			String data = this.readASCIIData(socket);
			System.out.println("ASCII data received.");
			socket.close();
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
	 * Reads the image data through a new data channel with active mode.
	 * 
	 * @return the image data sent by the client
	 * @throws IOException if a network error occured.
	 */
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

	/**
	 * Reads the image data through a new data channel with passive mode
	 * 
	 * @return the image data sent by the FTP client.
	 * @throws IOException if a network error occured.
	 */
	public byte[] readImagePassive() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(this.store.getPassivePort())) {
			serverSocket.setSoTimeout(10000); // set the timeout to 10 seconds.
			System.out.println("Waiting for client passive data channel...");
			Socket socket = serverSocket.accept();
			this.sendOpeningReply();

			System.out.println("Reading data through data channel.");
			byte[] data = this.readImageData(socket);
			System.out.println("ASCII data received.");
			socket.close();
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
	 * Writes the binary data though the data channel connection with the client.
	 * 
	 * @param imageData the data to write to the client.
	 * @param socket    the socket representing the data channel between the server
	 *                  and the client.
	 * @throws IOException
	 */
	private void writeImageData(byte[] imageData, Socket socket) throws IOException {
		try (DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())) {
			dataOut.write(imageData);
			dataOut.flush();
		}
	}

	/**
	 * Reads the ASCII data sent by the FTP client.
	 * 
	 * @param socket the socket representing the connection between the server and
	 *               the FTP client
	 * @return the ASCII data sent by the FTP client
	 * @throws IOException if a network error occured.
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
	 * Reads the Image data sent by the FTP client.
	 * 
	 * @param socket the socket representing the connection between the server and
	 *               the FTP client
	 * @return the Image data sent by the FTP client
	 * @throws IOException if a network error occured.
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
