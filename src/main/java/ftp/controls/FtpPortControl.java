package ftp.controls;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import ftp.FtpCommand;
import ftp.FtpReply;
import ftp.SessionStore;

/**
 * @author Sami BARCHID
 *
 *         Control class used to manage the FTP command "PORT"
 */
public class FtpPortControl extends FtpControl {

	/**
	 * @param store the store of the client's connection
	 */
	public FtpPortControl(SessionStore store) {
		super(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ftp.controls.FtpControl#handle(ftp.FtpCommand)
	 */
	@Override
	public FtpReply handle(FtpCommand command) {
		if (!this.store.isLoggedIn()) {
			return new FtpReply(5, 3, 0, "Please log in with USER and PASS first.");
		}

		String portArgs = command.getArg();
		if (portArgs == null) {
			return new FtpReply(5, 0, 1, "Syntax error");
		}

		InetSocketAddress adr = this.extractClientIp(portArgs);

		if (adr == null) {
			return new FtpReply(5, 0, 1, "Syntax error : port number not valid.");
		}

		this.store.setActiveAdr(adr);

		return new FtpReply(2, 0, 0, "Port command successful");
	}

	/**
	 * Extracts the IPv4 address specified in the command argument in parameter.
	 * 
	 * @param portArg the specified command argument
	 * @return the IPv4 address specified in the command argument or null if the
	 *         argument is not composed of .
	 */
	private InetSocketAddress extractClientIp(String portArg) {
		String[] args = portArg.split(",");
		if (args.length != 6) {
			return null;
		}
		try {
			byte num1 = Byte.parseByte(args[0]);
			byte num2 = Byte.parseByte(args[1]);
			byte num3 = Byte.parseByte(args[2]);
			byte num4 = Byte.parseByte(args[3]);

			int portNum1 = Integer.parseInt(args[4]);
			int portNum2 = Integer.parseInt(args[5]);
			int port = portNum1 * 256 + portNum2;

			System.out.println("Required port is " + port);

			if (!this.isPortValidAndAvailable(port)) {
				System.out.println("Error : port number " + port + " is not available. Client must try again.");
				return null;
			}

			InetAddress ip = Inet4Address.getByAddress(new byte[] { num1, num2, num3, num4 });
			return new InetSocketAddress(ip, port);
		} catch (NumberFormatException e) {
			return null;
		} catch (UnknownHostException e) {
			return null;
		}
	}

	/**
	 * Checks to see if a specific port is valid and available. This implementation
	 * is inspired by the Apache Camel project.
	 *
	 * @param port the port to check for availability
	 * @return true if the port is valid and available or else false
	 */
	private boolean isPortValidAndAvailable(int port) {
		if (port < 1024 || port > 65535) {
			System.out.println("Port is out of range.");
			return false;
		}

//		try (ServerSocket ss = new ServerSocket(port)) {
//		} catch (IOException e) {
//			System.out.println("Another process use this port.");
//			return false;
//		}
		return true;
	}
}
