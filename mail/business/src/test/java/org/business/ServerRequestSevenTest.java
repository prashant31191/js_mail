package org.business;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.Assert;
import org.cc.Serializer;
import org.cc.SimpleFolder;
import org.dms.Managing;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerRequestSevenTest {
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
	public void requestSevenIncorrectKeyTest() {
		try {
			byte[] requestBytes = Serializer.serialize("NewFolder");
			out.writeByte(7);
			out.writeInt(84693543);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 3);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestSevenIncorrectFolderNameTest() {
		try {
			Managing.createSession(3479026, "requestSevenBF@mail.js");
			String[] createInfo = { "requestSevenBF", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);

			byte[] requestBytes = Serializer.serialize("");
			out.writeByte(7);
			out.writeInt(3479026);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 1);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestSevenCorrectTest() {
		try {
			Managing.createSession(3896823, "requestSeven@mail.js");
			String[] createInfo = { "requestSeven", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);

			byte[] requestBytes = Serializer.serialize("NewFolder");
			out.writeByte(7);
			out.writeInt(3896823);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 0);
			int length = in.readInt();
			byte[] answerBytes = new byte[length];
			for (int i = 0; i < length; i++)
				answerBytes[i] = in.readByte();
			SimpleFolder folder = (SimpleFolder) Serializer
					.deserialize(answerBytes);
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
