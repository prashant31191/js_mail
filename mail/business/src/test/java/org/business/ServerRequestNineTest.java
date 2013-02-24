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

public class ServerRequestNineTest {
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
			socket = new Socket("localhost", 6709);
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
	public void requestNineIncorrectKeyTest() {
		try {
			byte[] requestBytes = Serializer.serialize(new Object[] {
					new SimpleMessage(), "", "" });
			out.writeByte(9);
			out.writeInt(2553335);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 3);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestNineIncorrectFolderTest() {
		try {
			Managing.createSession(5717246, "requestNineEF@mail.js");
			String[] createInfo = { "requestNineEF", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);
			List<SimpleFolder> folders = Managing
					.getEverything("requestNineEF@mail.js");
			SimpleMessage message = folders.get(0).getMessages().get(0);

			byte[] requestBytes = Serializer.serialize(new Object[] { message,
					"Входящие", "Входящие" });
			out.writeByte(9);
			out.writeInt(5717246);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 1);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestNineCorrectTest() {
		try {
			Managing.createSession(1199454, "requestNine@mail.js");
			String[] createInfo = { "requestNine", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);
			List<SimpleFolder> folders = Managing
					.getEverything("requestNine@mail.js");
			SimpleMessage message = folders.get(0).getMessages().get(0);

			byte[] requestBytes = Serializer.serialize(new Object[] { message,
					"Входящие", "Корзина" });
			out.writeByte(9);
			out.writeInt(1199454);
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
			server.main(new String[] {"6709"});
		}
	}
}
