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

public class ServerRequestSixTest {
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
			socket = new Socket("localhost", 6706);
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
	public void requestSixIncorrectTest() {
		try {
			byte[] requestBytes = Serializer.serialize(new Object[] {
					new SimpleMessage(), "Входящие" });
			out.writeByte(6);
			out.writeInt(54794356);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 3);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestSixCorrectTest() {
		try {
			Managing.createSession(8349827, "requestSix@mail.js");
			String[] createInfo = { "requestSix", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);
			List<SimpleFolder> folders = Managing
					.getEverything("requestSix@mail.js");
			SimpleFolder input = null;
			for (SimpleFolder folder : folders)
				if (folder.getName().equals("Входящие")) {
					input = folder;
					break;
				}

			byte[] requestBytes = Serializer.serialize(new Object[] {
					input.getMessages().get(0), "Входящие" });
			out.writeByte(6);
			out.writeInt(8349827);
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
			server.main(new String[] {"6706"});
		}
	}
}
