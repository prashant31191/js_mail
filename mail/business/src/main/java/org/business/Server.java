package org.business;

import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import org.dms.Managing;
import org.dms.Session;
import org.cc.*;

/**
 * Mail server
 * 
 * @author Fomin
 * @version 1.0
 */
public class Server {
	private final static int portNumber = 6789;
	/*Map для хранения пары клуч - почта*/

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
		
		new SessionChecker().start();
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + portNumber);
			System.exit(-1);
		}

		while (listening) {
			try {
				/*Ждем accept, затем отправляем Runnable в пул потоков*/
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
				/*Если это запрос на регистрацию*/
				if (requestNumber == 1) {
					/*Получаем регистрационные данные*/
					int length = in.readInt();
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					String[] requestInfo = (String[]) Serializer
							.deserialize(requestBytes);

					boolean[] checkedData = new UserValidation()
							.regDataCheck(requestInfo);
					
					if (!checkedData[0]) {
						/*Если данные некорректны, возвращаем маску*/
						byte[] answerBytes = Serializer.serialize(checkedData);
						out.writeByte(1);
						out.writeInt(answerBytes.length);
						out.write(answerBytes);
					} else {
						/*Пробуем создать пользователя*/
						String workWith = Managing.createUser(requestInfo);
						
						if (!workWith.equals("")) {
							/*Если пользователь успешно создан, отправляем ключ*/
							Random rand = new Random();
							int key = rand.nextInt(1000000);
							Managing.createSession(key, workWith);
							out.writeByte(0);
							out.writeInt(key);
						} else
							out.writeByte(2);
					}
				}
				/*Если запрос на вход в систему*/
				if (requestNumber == 2) {
					/*Получаем логин и пароль*/
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
						/*Если данные верны*/
						boolean userExists = Managing.checkUser(requestInfo[0],
								requestInfo[1]);

						if (userExists) {
							/*Если пользователь существует, отправляем ключ*/
							Random rand = new Random();
							int key = rand.nextInt(1000000);
							Managing.createSession(key, requestInfo[0]);
							out.writeByte(0);
							out.writeInt(key);
						} else {
							out.writeByte(2);
						}
					}
				}
				/*Запрос на все папки и письма, которые есть у пользователя*/
				if (requestNumber == 3) {
					/*Проверяем, есть ли такое ключ в сессиях*/
					int gotKey = in.readInt();
					if (!Managing.sessionExists(gotKey))
						out.writeByte(3);
					else {
						/*Если есть, пытаемся получить из БД данные*/
						List<SimpleFolder> answer = Managing
								.getEverything(Managing.getSessionUser(gotKey));
						if (answer == null)
							out.writeByte(2);
						else {
							/*Если успешно, отправляем данные пользователю*/
							byte[] answerBytes = Serializer.serialize(answer);
							out.writeByte(0);
							out.writeInt(answerBytes.length);
							out.write(answerBytes);
						}
					}

				}
				/*Запрос на отправку письма*/
				if (requestNumber == 4) {
					int gotKey = in.readInt();
					int length = in.readInt();
					/*Получаем данные о письме*/
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					String[] requestInfo = (String[]) Serializer
							.deserialize(requestBytes);
					/*Проверяем, есть ли такой ключ в сессиях*/
					if (!Managing.sessionExists(gotKey))
						out.writeByte(3);
					else {
						Managing.undateSessionLastRequest(gotKey);
						/*Если есть, проверяем данные*/
						boolean[] checkedData = new ActionValidation()
								.checkMessage(requestInfo);
						if (!checkedData[0]) {
							/*Если данные некорректны, отправляем маску*/
							byte[] answerBytes = Serializer
									.serialize(checkedData);
							out.writeByte(1);
							out.writeInt(answerBytes.length);
							out.write(answerBytes);
						} else {
							/*Пытаемся отправить письмо*/
							Object[] answer = Managing.sendMessage(requestInfo,
									Managing.getSessionUser(gotKey));
							if (answer == null) {
								out.writeByte(2);
							} else {
								/*Если письмо отправлено, отправляем пользователю новые письма*/
								byte[] answerBytes = Serializer
										.serialize(answer);
								out.writeByte(0);
								out.writeInt(answerBytes.length);
								out.write(answerBytes);
							}
						}
					}
				}
				/*Запрос на отметку письма как прочитанное*/
				if (requestNumber == 5) {
					int gotKey = in.readInt();
					int length = in.readInt();
					/*Получаем письмо*/
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					SimpleMessage requestInfo = (SimpleMessage) Serializer
							.deserialize(requestBytes);

					if (!Managing.sessionExists(gotKey))
						out.writeByte(3);
					else {
						Managing.undateSessionLastRequest(gotKey);
						/*Если ключ есть в сессиях, пытаемся отметить как прочитанное*/
						boolean set = Managing.setRead(requestInfo, Managing.getSessionUser(gotKey));
						if (set) {
							out.writeByte(0);
						} else
							out.writeByte(2);
					}
				}
				/*Запрос на удаление письма*/
				if (requestNumber == 6) {
					int gotKey = in.readInt();
					int length = in.readInt();
					/*Получаем данные о письме*/
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					Object[] requestInfo = (Object[]) Serializer
							.deserialize(requestBytes);
					
					if (!Managing.sessionExists(gotKey))
						out.writeByte(3);
					else {
						Managing.undateSessionLastRequest(gotKey);
						/*Если ключ есть в сессиях, пытаемся удалить письмо*/
						boolean deleted = Managing.deleteMess(
								(SimpleMessage) requestInfo[0],
								(String) requestInfo[1], Managing.getSessionUser(gotKey));
						if (deleted) {
							out.writeByte(0);
						} else
							out.writeByte(2);
					}
				}
				/*Запрос на создание новой папки*/
				if (requestNumber == 7) {
					int gotKey = in.readInt();
					int length = in.readInt();
					/*Получаем название папки*/
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					String requestInfo = (String) Serializer
							.deserialize(requestBytes);

					if (!Managing.sessionExists(gotKey))
						out.writeByte(3);
					else {
						Managing.undateSessionLastRequest(gotKey);
						/*Если ключ есть в сессиях, проверяем корректность названия*/
						boolean correctFolder = new ActionValidation()
								.checkFolderName(requestInfo);
						if (!correctFolder)
							out.writeByte(1);
						else {
							/*Если название корректно, пытаемся создать папку*/
							SimpleFolder created = Managing.createFolder(
									requestInfo, Managing.getSessionUser(gotKey));
							if (created != null) {
								/*Если папка создана, отправляем ее*/
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
				/*Запрос на удаление папки*/
				if (requestNumber == 8) {
					int gotKey = in.readInt();
					int length = in.readInt();
					/*Получаем папку*/
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					SimpleFolder requestInfo = (SimpleFolder) Serializer
							.deserialize(requestBytes);

					if (!Managing.sessionExists(gotKey))
						out.writeByte(3);
					else {
						Managing.undateSessionLastRequest(gotKey);
						/*Если ключ есть в сессиях, пробуем удалить папку*/
						byte deleted = Managing.deleteFolder(requestInfo,
								Managing.getSessionUser(gotKey));
						if (deleted == 1)
							out.writeByte(1);
						else if (deleted == 2) {
							out.writeByte(2);
						} else if (deleted == 0) {
							out.writeByte(0);
						}
					}
				}
				/*Запрос на перемещение письма*/
				if (requestNumber == 9) {
					int gotKey = in.readInt();
					int length = in.readInt();
					/*Получаем данные о письме*/
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					Object[] requestInfo = (Object[]) Serializer
							.deserialize(requestBytes);

					if (!Managing.sessionExists(gotKey))
						out.writeByte(3);
					else {
						Managing.undateSessionLastRequest(gotKey);
						/*Если ключ есть в сессиях, пробуем переместить письмо*/
						SimpleMessage message = (SimpleMessage) requestInfo[0];
						String folder = (String) requestInfo[1];
						String moveFolder = (String) requestInfo[2];

						if (folder.equals(moveFolder))
							out.writeByte(1);
						else {
							boolean moved = Managing.moveMessage(message,
									folder, moveFolder, Managing.getSessionUser(gotKey));
							if (moved)
								out.writeByte(0);
							else
								out.writeByte(2);
						}
					}
				}
				/*Запрос на проверку новых сообещений*/
				if (requestNumber == 10) {
					int gotKey = in.readInt();
					/*Получаем количество сообщений у пользователя*/
					int amount = in.readInt();
					
					if (!Managing.sessionExists(gotKey))
						out.writeByte(3);
					else {
						/*Если ключ есть в сессиях, проверяем есть ли новые сообщения*/
						int amountOfNew = Managing.hasNewMessages(amount,
								Managing.getSessionUser(gotKey));
						if (amountOfNew == -1)
							out.writeByte(2);
						else {
							/*Если есть новые, получаем их и отправляем пользователю*/
							if (amountOfNew == 0)
								out.writeByte(1);
							else {
								List<SimpleMessage> newMessages = Managing
										.getNewMessages(amountOfNew, Managing.getSessionUser(gotKey));
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
				/*Запрос на выход из системы*/
				if (requestNumber == 0) {
					int gotKey = in.readInt();
					if (Managing.sessionExists(gotKey)) {
						Managing.deleteSession(gotKey);
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
	/*Поток для работы с сессиями*/
	private class SessionChecker extends Thread {
		SessionChecker() {
			super();
			setDaemon(true);
		}
		
		public void run() {
			List<Session> sessions = null;
			while (true) {
				/*Ждем*/
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				/*Берем все сессии*/
				sessions = Managing.getSessions();
				if (sessions != null) {
					long currentDate = System.currentTimeMillis();
					long sessionDate = 0;
					/*Просматриваем все сессии*/
					for (Session session : sessions) {
						sessionDate = session.getTime().getTime();
						if (currentDate - sessionDate > 1000 * 60 * 5) {
							/*Удаляем неактивного пользователя*/
							Managing.deleteSession(session.getId());
						}
					}
				}
			}
		}
	}
}