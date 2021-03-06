package org.business;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import junit.framework.Assert;

import org.cc.Serializer;
import org.cc.SimpleFolder;
import org.cc.SimpleMessage;
import org.dms.Managing;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerRequestFiveTest {
	private Socket socket = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;

	@BeforeClass
	public static void beforeClass() {
		ServerThread serverThread = new ServerThread();
		serverThread.setDaemon(true);
		serverThread.start();
	}

	@Before
	public void beforeTest() {
		try {
			socket = new Socket("localhost", 6705);
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		try {
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e2) {
			e2.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e2) {
			e2.printStackTrace();
			try {
				out.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Test
	public void requestFiveIncorrectTest() {
		try {
			byte[] requestBytes = Serializer.serialize(new SimpleMessage());
			out.writeByte(5);
			out.writeInt(54763356);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 3);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void requestFiveCorrectTest() {
		try {
			Managing.createSession(54763356, "requestFive@mail.js");
			String[] createInfo = { "requestFive", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);
			List<SimpleFolder> folders = Managing.getEverything("requestFive@mail.js");
			SimpleFolder input = null;
			for (SimpleFolder folder : folders)
				if (folder.getName().equals("Входящие")) {
					input = folder;
					break;
				}
			
			byte[] requestBytes = Serializer.serialize(input.getMessages().get(0));
			out.writeByte(5);
			out.writeInt(54763356);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 0);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@After
	public void afterTest() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	static class ServerThread extends Thread {
		@Override
		@SuppressWarnings("static-access")
		public void run() {
			Server server = new Server();
			server.main(new String[] {"6705"});
		}
	}
}
