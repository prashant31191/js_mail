package org.business;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import junit.framework.Assert;
import org.cc.Serializer;
import org.cc.SimpleMessage;
import org.dms.Managing;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerRequestTenTest {
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
			socket = new Socket("localhost", 6789);
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
	public void requestTenIncorrectKeyTest() {
		try {
			out.writeByte(10);
			out.writeInt(43773657);
			out.writeInt(5);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 3);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestTenNoMessagesTest() {
		try {
			Managing.createSession(609091265, "requestTenNM@mail.js");
			String[] createInfo = { "requestTenNM", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);

			out.writeByte(10);
			out.writeInt(609091265);
			out.writeInt(1);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 1);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestTenCorrectTest() {
		try {
			Managing.createSession(6121265, "requestTen@mail.js");
			String[] createInfo = { "requestTen", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);

			out.writeByte(10);
			out.writeInt(6121265);
			out.writeInt(0);

			byte answer = in.readByte();

			int length = in.readInt();
			byte[] answerBytes = new byte[length];
			for (int i = 0; i < length; i++)
				answerBytes[i] = in.readByte();
			@SuppressWarnings("unchecked")
			List<SimpleMessage> gotMessages = (List<SimpleMessage>) Serializer
					.deserialize(answerBytes);
			Assert.assertTrue(1 == gotMessages.size());
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
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
		@SuppressWarnings("static-access")
		public void run() {
			Server server = new Server();
			server.main(null);
		}
	}
}
