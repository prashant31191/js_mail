package org.business;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.cc.Serializer;
import org.cc.SimpleMessage;
import org.dms.Managing;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerRequestFourTest {
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
			socket = new Socket("localhost", 6704);
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
	public void requestFourIncorrectKeyTest() {
		try {
			String[] requestInfo = { "somemail@mail.js", "hi", "hi there" };

			byte[] requestBytes = Serializer.serialize(requestInfo);
			out.writeByte(4);
			out.writeInt(3534534);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 3);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestFourIncorrectDataTest() {
		try {
			Managing.createSession(123453, "requestFour@mail.js");

			String[] requestInfo = { "somemail", "", "" };

			byte[] requestBytes = Serializer.serialize(requestInfo);
			out.writeByte(4);
			out.writeInt(123453);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 1);

			int length = in.readInt();
			byte[] answerBytes = new byte[length];
			for (int i = 0; i < length; i++)
				answerBytes[i] = in.readByte();
			boolean[] data = (boolean[]) Serializer.deserialize(answerBytes);
			Assert.assertTrue(Arrays.equals(data, new boolean[] { false, false,
					false, false }));
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void requestFourCorrectTest() {
		try {
			Managing.createSession(1234563, "requestFour@mail.js");
			String[] createInfo = { "requestFour", "12341234", "12341234",
					"Joe", "Johns", "12.12.1999", "1234321" };
			Managing.createUser(createInfo);
			
			String[] requestInfo = { "somemail@mail.js", "Hi", "Hi there" };
			byte[] requestBytes = Serializer.serialize(requestInfo);
			out.writeByte(4);
			out.writeInt(1234563);
			out.writeInt(requestBytes.length);
			out.write(requestBytes);

			byte answer = in.readByte();
			Assert.assertTrue(answer == 0);

			int length = in.readInt();
			byte[] answerBytes = new byte[length];
			for (int i = 0; i < length; i++)
				answerBytes[i] = in.readByte();
			Object[] messages = (Object[]) Serializer
					.deserialize(answerBytes);
			@SuppressWarnings("unchecked")
			List<SimpleMessage> incomes = (List<SimpleMessage>)messages[1];
			Assert.assertTrue(incomes.size() == 1);
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
		@Override
		@SuppressWarnings("static-access")
		public void run() {
			Server server = new Server();
			server.main(new String[] {"6704"});
		}
	}
}
