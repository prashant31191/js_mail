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
import org.dms.Managing;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerRequestEightTest {
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
	public void requestEightIncorrectKeyTest() {
		try {
			byte[] requestBytes = Serializer.serialize(new SimpleFolder());
			out.writeByte(8);
			out.writeInt(353632);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 3);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestEightIncorrectFolderTest() {
		try {
			Managing.createSession(3479943, "requestEightIF@mail.js");
			String[] createInfo = { "requestEightIF", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);
			List<SimpleFolder> folders = Managing
					.getEverything("requestEightIF@mail.js");

			byte[] requestBytes = Serializer.serialize(folders.get(0));
			out.writeByte(8);
			out.writeInt(3479943);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 1);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestEightCorrectTest() {
		try {
			Managing.createSession(4363225, "requestEight@mail.js");
			String[] createInfo = { "requestEight", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);
			SimpleFolder folder = Managing.createFolder("NewFolder",
					"requestEight@mail.js");

			byte[] requestBytes = Serializer.serialize(folder);
			out.writeByte(8);
			out.writeInt(4363225);
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
		@SuppressWarnings("static-access")
		public void run() {
			Server server = new Server();
			server.main(null);
		}
	}
}
