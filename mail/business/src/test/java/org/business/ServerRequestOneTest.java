package org.business;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import junit.framework.Assert;

import org.cc.Serializer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerRequestOneTest {
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
	public void requestOneIncorrectDataTest() {
		try {
			String[] requestInfo = { "", "", "", "", "", "", "" };

			byte[] requestBytes = Serializer.serialize(requestInfo);
			out.writeByte(1);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 1);

			int length = in.readInt();
			byte[] answerBytes = new byte[length];
			for (int i = 0; i < length; i++)
				answerBytes[i] = in.readByte();
			boolean[] answers = (boolean[]) Serializer.deserialize(answerBytes);
			Assert.assertTrue(Arrays.equals(answers, new boolean[] { false,
					false, false, false, true, false, false, false, false }));
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestOneCorrectDataTest() {
		try {
			String[] requestInfo = { "requestOne", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };

			byte[] requestBytes = Serializer.serialize(requestInfo);
			out.writeByte(1);
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
	public void requestOneCorrectDataDoubleTest() {
		try {
			String[] requestInfo = { "requestOne", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };

			byte[] requestBytes = Serializer.serialize(requestInfo);
			out.writeByte(1);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 2);

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
