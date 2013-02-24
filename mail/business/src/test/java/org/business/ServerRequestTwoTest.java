package org.business;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.cc.Serializer;
import org.dms.Managing;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerRequestTwoTest {
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
			socket = new Socket("localhost", 6702);
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
	public void requestTwoIncorrectTest() {
		try {
			String[] requestInfo = { "norep@mail.js", "somePass" };

			byte[] requestBytes = Serializer.serialize(requestInfo);
			out.writeByte(2);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);
			byte answer = in.readByte();

			Assert.assertTrue(answer == 2);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestTwoCorrectTest() {
		String[] data = { "requestTwo", "12344321", "12344321", "Joe",
				"Johnson", "12.12.1989", "4634345" };
		Managing.createUser(data);

		try {
			String[] requestInfo = { "requestTwo@mail.js", "12344321" };

			byte[] requestBytes = Serializer.serialize(requestInfo);
			out.writeByte(2);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 0);

			int key = in.readInt();
			Assert.assertTrue(key > -1);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestTwoDoupleLogInTest() {
		try {
			String[] requestInfo = { "requestTwo@mail.js", "12344321" };

			byte[] requestBytes = Serializer.serialize(requestInfo);
			out.writeByte(2);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 3);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestTwoIncorrectDataTest() {
		try {
			String[] requestInfo = { "", "" };

			byte[] requestBytes = Serializer.serialize(requestInfo);
			out.writeByte(2);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);
			byte answer = in.readByte();

			Assert.assertTrue(answer == 1);
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
			server.main(new String[] {"6702"});
		}
	}
}
