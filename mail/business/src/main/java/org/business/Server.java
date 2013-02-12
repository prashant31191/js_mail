package org.business;

import java.net.*;
import java.util.Random;
import java.io.*;

public class Server {
	private final static int portNumber = 6789;

	public static void main(String[] args) {
		try {
			new Server().startServer();
		} catch (Exception e) {
			System.out.println("I/O failure: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void startServer() throws Exception {
		ServerSocket serverSocket = null;
		boolean listening = true;

		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + portNumber);
			System.exit(-1);
		}

		while (listening) {
			try {
				new ConnectionRequestHandler(serverSocket.accept()).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		serverSocket.close();
	}

	private class ConnectionRequestHandler extends Thread {
		private Socket socket = null;
		private DataOutputStream out = null;
		private DataInputStream in = null;
		private int key = 0;
		private String workWith = null;

		public ConnectionRequestHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			System.out.println("Client connected to socket: "
					+ socket.toString());

			Random rand = new Random();
			key = rand.nextInt(1000000);

			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				System.out.println("Socket stream error");
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			byte requestNumber = 0;

			try {
				while ((requestNumber = in.readByte()) != 0) {
					if (requestNumber == 1) {

						int length = in.readInt();
						byte[] requestBytes = new byte[length];

						for (int i = 0; i < length; i++)
							requestBytes[i] = in.readByte();
						String[] requestInfo = (String[]) Serializer
								.deserialize(requestBytes);

						boolean[] checkedData = new UserValidation()
								.regDataCheck(requestInfo);

						if (checkedData[0]) {
							out.writeBoolean(true);
							out.writeInt(key);
							workWith = requestInfo[0] + "@mail.js";
						} else {
							byte[] answerBytes = Serializer
									.serialize(checkedData);
							out.writeBoolean(false);
							out.writeInt(answerBytes.length);
							out.write(answerBytes);
						}
					}

					if (requestNumber == 2) {
						int length = in.readInt();
						byte[] requestBytes = new byte[length];

						for (int i = 0; i < length; i++)
							requestBytes[i] = in.readByte();
						String[] requestInfo = (String[]) Serializer
								.deserialize(requestBytes);

						boolean userChecked = new UserValidation()
								.enterUserCheck(requestInfo);

						if (userChecked) {
							out.writeBoolean(true);
							out.writeInt(key);
							workWith = requestInfo[0];
						} else {
							out.writeBoolean(false);
						}
					}

					if (requestNumber == 3) {
						int length = in.readInt();
						byte[] requestBytes = new byte[length];

						for (int i = 0; i < length; i++)
							requestBytes[i] = in.readByte();
						String requestInfo = (String) Serializer
								.deserialize(requestBytes);

						String tmp = requestInfo + "HAHA";
						byte[] answerBytes = Serializer.serialize(tmp);

						out.writeBoolean(true);
						out.writeInt(answerBytes.length);
						out.write(answerBytes);
					}
				}
			} catch (IOException io) {

			} catch (ClassNotFoundException cnf) {

			} finally {
				try {
					in.close();
					out.close();
					socket.close();
				} catch (IOException e) {
					System.out.println("Closing error");
					e.printStackTrace();
				}

			}
		}

	}
}