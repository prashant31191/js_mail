package org.business;

import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import org.dms.Managing;
import org.cc.*;

/**
 * Mail server
 * 
 * @author Fomin
 * @version 1.0
 */
public class Server {
	private final static int portNumber = 6789;
	Map<Integer, String> users = new HashMap<Integer, String>();

	/**
	 * Main method for starting server
	 * 
	 * @param args
	 */
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
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + portNumber);
			System.exit(-1);
		}

		while (listening) {
			try {
				executor.execute(new ConnectionRequestHandler(serverSocket.accept()));
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

		/**
		 * Public constructor for request hendler
		 * 
		 * @param socket
		 *            Accepted socket
		 */
		public ConnectionRequestHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			System.out.println("Client connected to socket: "
					+ socket.toString());

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

			byte requestNumber = -1;

			try {
				requestNumber = in.readByte();
				if (requestNumber == 1) {

					int length = in.readInt();
					byte[] requestBytes = new byte[length];

					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					String[] requestInfo = (String[]) Serializer
							.deserialize(requestBytes);

					boolean[] checkedData = new UserValidation()
							.regDataCheck(requestInfo);

					if (!checkedData[0]) {
						byte[] answerBytes = Serializer.serialize(checkedData);
						out.writeByte(1);
						out.writeInt(answerBytes.length);
						out.write(answerBytes);
					} else {
						String workWith = Managing.createUser(requestInfo);
						if (!workWith.equals("")) {
							Random rand = new Random();
							int key = rand.nextInt(1000000);
							users.put(key, workWith);
							out.writeByte(0);
							out.writeInt(key);
						} else
							out.writeByte(2);
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

					if (!userChecked) {
						out.writeByte(1);
					} else {
						boolean userExists = Managing.checkUser(requestInfo[0],
								requestInfo[1]);

						if (userExists) {
							Random rand = new Random();
							int key = rand.nextInt(1000000);
							users.put(key, requestInfo[0]);
							out.writeByte(0);
							out.writeInt(key);
						} else {
							out.writeByte(2);
						}
					}
				}

				if (requestNumber == 3) {
					int gotKey = in.readInt();
					if (!users.containsKey(gotKey))
						out.writeByte(3);
					else {
						List<SimpleFolder> answer = Managing
								.getEverything(users.get(gotKey));
						if (answer == null)
							out.writeByte(2);
						else {
							byte[] answerBytes = Serializer.serialize(answer);
							out.writeByte(0);
							out.writeInt(answerBytes.length);
							out.write(answerBytes);
						}
					}

				}

				if (requestNumber == 4) {
					int gotKey = in.readInt();
					int length = in.readInt();
					byte[] requestBytes = new byte[length];

					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					String[] requestInfo = (String[]) Serializer
							.deserialize(requestBytes);

					if (!users.containsKey(gotKey))
						out.writeByte(3);
					else {
						boolean[] checkedData = new ActionValidation()
								.checkMessage(requestInfo);
						if (!checkedData[0]) {
							byte[] answerBytes = Serializer
									.serialize(checkedData);
							out.writeByte(1);
							out.writeInt(answerBytes.length);
							out.write(answerBytes);
						} else {
							Object[] answer = Managing.sendMessage(requestInfo,
									users.get(gotKey));
							if (answer == null) {
								out.writeByte(2);
							} else {
								byte[] answerBytes = Serializer
										.serialize(answer);
								out.writeByte(0);
								out.writeInt(answerBytes.length);
								out.write(answerBytes);
							}
						}
					}
				}

				if (requestNumber == 5) {
					int gotKey = in.readInt();
					int length = in.readInt();
					byte[] requestBytes = new byte[length];

					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					SimpleMessage requestInfo = (SimpleMessage) Serializer
							.deserialize(requestBytes);

					if (!users.containsKey(gotKey))
						out.writeByte(3);
					else {
						boolean set = Managing.setRead(requestInfo, users.get(gotKey));
						if (set) {
							out.writeByte(0);
						} else
							out.writeByte(2);
					}
				}
				if (requestNumber == 6) {
					int gotKey = in.readInt();
					int length = in.readInt();
					byte[] requestBytes = new byte[length];

					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					Object[] requestInfo = (Object[]) Serializer
							.deserialize(requestBytes);
					
					if (!users.containsKey(gotKey))
						out.writeByte(3);
					else {
						boolean deleted = Managing.deleteMess(
								(SimpleMessage) requestInfo[0],
								(String) requestInfo[1], users.get(gotKey));
						if (deleted) {
							out.writeByte(0);
						} else
							out.writeByte(2);
					}
				}

				if (requestNumber == 7) {
					int gotKey = in.readInt();
					int length = in.readInt();
					byte[] requestBytes = new byte[length];

					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					String requestInfo = (String) Serializer
							.deserialize(requestBytes);

					if (!users.containsKey(gotKey))
						out.writeByte(3);
					else {
						boolean correctFolder = new ActionValidation()
								.checkFolderName(requestInfo);
						if (!correctFolder)
							out.writeByte(1);
						else {
							SimpleFolder created = Managing.createFolder(
									requestInfo, users.get(gotKey));
							if (created != null) {
								byte[] answerBytes = Serializer
										.serialize(created);
								out.writeByte(0);
								out.writeInt(answerBytes.length);
								out.write(answerBytes);
							} else
								out.writeByte(4);
						}
					}
				}

				if (requestNumber == 8) {
					int gotKey = in.readInt();
					int length = in.readInt();
					byte[] requestBytes = new byte[length];

					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					SimpleFolder requestInfo = (SimpleFolder) Serializer
							.deserialize(requestBytes);

					if (!users.containsKey(gotKey))
						out.writeByte(3);
					else {
						byte deleted = Managing.deleteFolder(requestInfo,
								users.get(gotKey));
						if (deleted == 1)
							out.writeByte(1);
						else if (deleted == 2) {
							out.writeByte(2);
						} else if (deleted == 0) {
							out.writeByte(0);
						}
					}
				}

				if (requestNumber == 9) {
					int gotKey = in.readInt();
					int length = in.readInt();
					byte[] requestBytes = new byte[length];

					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					Object[] requestInfo = (Object[]) Serializer
							.deserialize(requestBytes);

					if (!users.containsKey(gotKey))
						out.writeByte(3);
					else {
						SimpleMessage message = (SimpleMessage) requestInfo[0];
						String folder = (String) requestInfo[1];
						String moveFolder = (String) requestInfo[2];

						if (folder.equals(moveFolder))
							out.writeByte(1);
						else {
							boolean moved = Managing.moveMessage(message,
									folder, moveFolder, users.get(gotKey));
							if (moved)
								out.writeByte(0);
							else
								out.writeByte(2);
						}
					}
				}

				if (requestNumber == 10) {
					int gotKey = in.readInt();
					int amount = in.readInt();

					if (!users.containsKey(gotKey))
						out.writeByte(3);
					else {
						int amountOfNew = Managing.hasNewMessages(amount,
								users.get(gotKey));
						if (amountOfNew == -1)
							out.writeByte(2);
						else {
							if (amountOfNew == 0)
								out.writeByte(1);
							else {
								List<SimpleMessage> newMessages = Managing
										.getNewMessages(amountOfNew, users.get(gotKey));
								if (newMessages == null)
									out.writeByte(2);
								else {
									for (SimpleMessage mess : newMessages)
										System.out.println(mess.getText());
									byte[] answerBytes = Serializer
											.serialize(newMessages);
									out.writeByte(0);
									out.writeInt(answerBytes.length);
									out.write(answerBytes);
								}
							}
						}
					}
				}
				
				if (requestNumber == 0) {
					int gotKey = in.readInt();
					if (users.containsKey(gotKey)) {
						users.remove(gotKey);
						System.out.println("User removed");
					}
				}
			} catch (IOException io) {
				io.printStackTrace();
			} catch (ClassNotFoundException cnf) {
				cnf.printStackTrace();
			} finally {
				try {
					in.close();
					out.close();
					socket.close();
					System.out.println(socket.getPort() + " - closed");
				} catch (IOException e) {
					System.out.println("Closing error");
					e.printStackTrace();
				}

			}
		}
	}
}