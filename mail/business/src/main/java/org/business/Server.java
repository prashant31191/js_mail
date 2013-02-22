package org.business;

import java.net.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import org.dms.Managing;
import org.dms.Session;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.cc.*;

/**
 * Mail server
 * 
 * @author Fomin
 * @version 1.0
 */
public class Server {
	private static final Logger logger = Logger.getLogger("Server");
	private final static int portNumber = 6789;

	/**
	 * Main method for starting server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		logger.setLevel(Level.INFO);
		try {
			new Server().startServer();
			logger.info("Server started");
		} catch (Exception e) {
			logger.error("I/O failure: ", e);
			e.printStackTrace();
		}
	}

	private void startServer() throws Exception {
		ServerSocket serverSocket = null;
		boolean listening = true;

		logger.info("Starting session checker");
		new SessionChecker().start();
		logger.info("Session checker is started");

		ExecutorService executor = Executors.newFixedThreadPool(10);
		try {
			serverSocket = new ServerSocket(portNumber);
			logger.info("Socket created: " + portNumber);
		} catch (IOException e) {
			logger.error("Could not listen on port: " + portNumber);
			System.exit(-1);
		}

		while (listening) {
			try {
				/* Ждем accept, затем отправляем Runnable в пул потоков */
				executor.execute(new ConnectionRequestHandler(serverSocket
						.accept()));
				logger.info("Accepted");
			} catch (IOException e) {
				logger.error("Could not accept", e);
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
			logger.info("Client connected to socket: " + socket.toString());

			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				logger.error("Socket stream error", e);
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
				/* Если это запрос на регистрацию */
				if (requestNumber == 1) {
					logger.info("Got request for registration ["
							+ socket.getPort() + "]");
					logger.info("Trying to get reg data [" + socket.getPort()
							+ "]");
					/* Получаем регистрационные данные */
					int length = in.readInt();
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					String[] requestInfo = (String[]) Serializer
							.deserialize(requestBytes);
					logger.info("Got reg data [" + socket.getPort() + "]");

					boolean[] checkedData = new UserValidation()
							.regDataCheck(requestInfo);

					if (!checkedData[0]) {
						logger.info("Sending mask of reg data ["
								+ socket.getPort() + "]");
						/* Если данные некорректны, возвращаем маску */
						byte[] answerBytes = Serializer.serialize(checkedData);
						out.writeByte(1);
						out.writeInt(answerBytes.length);
						out.write(answerBytes);
						logger.info("Sent mask of reg data ["
								+ socket.getPort() + "]");
					} else {
						logger.info("Trying to create user ["
								+ socket.getPort() + "]");
						/* Пробуем создать пользователя */
						String workWith = Managing.createUser(requestInfo);

						if (!workWith.equals("")) {
							logger.info("Trying to send new session key ["
									+ socket.getPort() + "]");
							/* Если пользователь успешно создан, отправляем ключ */
							Random rand = new Random();
							int key = rand.nextInt(1000000);
							Managing.createSession(key, workWith);
							out.writeByte(0);
							out.writeInt(key);
							logger.info("Sent key [" + socket.getPort() + "]");
						} else {
							logger.info("Could not create new user ["
									+ socket.getPort() + "]");
							out.writeByte(2);
						}
					}
				}
				/* Если запрос на вход в систему */
				if (requestNumber == 2) {
					logger.info("Got request to log in [" + socket.getPort()
							+ "]");
					logger.info("Trying to get data [" + socket.getPort() + "]");
					/* Получаем логин и пароль */
					int length = in.readInt();
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					String[] requestInfo = (String[]) Serializer
							.deserialize(requestBytes);
					logger.info("Got data " + socket.getPort());
					logger.info("Checking data correctness ["
							+ socket.getPort() + "]");
					boolean userChecked = new UserValidation()
							.enterUserCheck(requestInfo);

					if (!userChecked) {
						logger.info("Data is incorrect [" + socket.getPort()
								+ "]");
						out.writeByte(1);
					} else {
						/* Если данные верны */
						logger.info("Trying to check user in DB ["
								+ socket.getPort() + "]");
						boolean userExists = Managing.checkUser(requestInfo[0],
								requestInfo[1]);

						if (userExists) {
							logger.info("Trying to send session key ["
									+ socket.getPort() + "]");
							/* Если пользователь существует, отправляем ключ */
							Random rand = new Random();
							int key = rand.nextInt(1000000);
							Managing.createSession(key, requestInfo[0]);
							out.writeByte(0);
							out.writeInt(key);
							logger.info("Sent session key [" + socket.getPort()
									+ "]");
						} else {
							logger.info("Wrong user data [" + socket.getPort()
									+ "]");
							out.writeByte(2);
						}
					}
				}
				/* Запрос на все папки и письма, которые есть у пользователя */
				if (requestNumber == 3) {
					logger.info("Request to get everything ["
							+ socket.getPort() + "]");
					logger.info("Trying to get key [" + socket.getPort() + "]");
					/* Проверяем, есть ли такое ключ в сессиях */
					int gotKey = in.readInt();
					logger.info("Got key [" + socket.getPort() + "]");
					if (!Managing.sessionExists(gotKey)) {
						logger.info("The key does not exist ["
								+ socket.getPort() + "]");
						out.writeByte(3);
					} else {
						logger.info("Trying to get folders and messages from DB ["
								+ socket.getPort() + "]");
						/* Если есть, пытаемся получить из БД данные */
						List<SimpleFolder> answer = Managing
								.getEverything(Managing.getSessionUser(gotKey));
						if (answer == null) {
							logger.info("Could not got folders and messages ["
									+ socket.getPort() + "]");
							out.writeByte(2);
						} else {
							logger.info("Got folders and messages ["
									+ socket.getPort() + "]");
							logger.info("Trying to send folders and messages ["
									+ socket.getPort() + "]");
							/* Если успешно, отправляем данные пользователю */
							byte[] answerBytes = Serializer.serialize(answer);
							out.writeByte(0);
							out.writeInt(answerBytes.length);
							out.write(answerBytes);
							logger.info("Sent folders and messages ["
									+ socket.getPort() + "]");
						}
					}
				}
				/* Запрос на отправку письма */
				if (requestNumber == 4) {
					logger.info("Request to send message [" + socket.getPort()
							+ "]");
					logger.info("Trying to got key and message data ["
							+ socket.getPort() + "]");
					int gotKey = in.readInt();
					int length = in.readInt();
					/* Получаем данные о письме */
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					String[] requestInfo = (String[]) Serializer
							.deserialize(requestBytes);
					logger.info("Got key and message data [" + socket.getPort()
							+ "]");
					/* Проверяем, есть ли такой ключ в сессиях */
					if (!Managing.sessionExists(gotKey)) {
						logger.info("Key does not exist [" + socket.getPort()
								+ "]");
						out.writeByte(3);
					} else {
						logger.info("Updating session time  ["
								+ socket.getPort() + "]");
						Managing.undateSessionLastRequest(gotKey);
						logger.info("Checking correctness of data ["
								+ socket.getPort() + "]");
						/* Если есть, проверяем данные */
						boolean[] checkedData = new ActionValidation()
								.checkMessage(requestInfo);
						if (!checkedData[0]) {
							logger.info("Data is incorrect ["
									+ socket.getPort() + "]");
							logger.info("Trying to send mask ["
									+ socket.getPort() + "]");
							/* Если данные некорректны, отправляем маску */
							byte[] answerBytes = Serializer
									.serialize(checkedData);
							out.writeByte(1);
							out.writeInt(answerBytes.length);
							out.write(answerBytes);
							logger.info("Sent mask [" + socket.getPort() + "]");
						} else {
							logger.info("Trying to send message ["
									+ socket.getPort() + "]");
							/* Пытаемся отправить письмо */
							Object[] answer = Managing.sendMessage(requestInfo,
									Managing.getSessionUser(gotKey));
							if (answer == null) {
								logger.info("Message is not sent ["
										+ socket.getPort() + "]");
								out.writeByte(2);
							} else {
								logger.info("Trying to send new messages to user ["
										+ socket.getPort() + "]");
								/*
								 * Если письмо отправлено, отправляем
								 * пользователю новые письма
								 */
								byte[] answerBytes = Serializer
										.serialize(answer);
								out.writeByte(0);
								out.writeInt(answerBytes.length);
								out.write(answerBytes);
								logger.info("Sent messages ["
										+ socket.getPort() + "]");
							}
						}
					}
				}
				/* Запрос на отметку письма как прочитанное */
				if (requestNumber == 5) {
					logger.info("Request to set message as read ["
							+ socket.getPort() + "]");
					logger.info("Trying to get key and message ["
							+ socket.getPort() + "]");
					int gotKey = in.readInt();
					int length = in.readInt();
					/* Получаем письмо */
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					SimpleMessage requestInfo = (SimpleMessage) Serializer
							.deserialize(requestBytes);
					logger.info("Got message and key [" + socket.getPort()
							+ "]");
					if (!Managing.sessionExists(gotKey)) {
						logger.info("Key does not exist [" + socket.getPort()
								+ "]");
						out.writeByte(3);
					} else {
						logger.info("Updating session time ["
								+ socket.getPort() + "]");
						Managing.undateSessionLastRequest(gotKey);
						/*
						 * Если ключ есть в сессиях, пытаемся отметить как
						 * прочитанное
						 */
						logger.info("Trying to set message as read ["
								+ socket.getPort() + "]");
						boolean set = Managing.setRead(requestInfo,
								Managing.getSessionUser(gotKey));
						if (set) {
							logger.info("Message is set [" + socket.getPort()
									+ "]");
							out.writeByte(0);
						} else {
							logger.info("Message is not set ["
									+ socket.getPort() + "]");
							out.writeByte(2);
						}
					}
				}
				/* Запрос на удаление письма */
				if (requestNumber == 6) {
					logger.info("Request to delete message ["
							+ socket.getPort() + "]");
					logger.info("Trying to get message and key ["
							+ socket.getPort() + "]");
					int gotKey = in.readInt();
					int length = in.readInt();
					/* Получаем данные о письме */
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					Object[] requestInfo = (Object[]) Serializer
							.deserialize(requestBytes);
					logger.info("Got message and key [" + socket.getPort()
							+ "]");
					if (!Managing.sessionExists(gotKey)) {
						logger.info("Key does not exist [" + socket.getPort()
								+ "]");
						out.writeByte(3);
					} else {
						logger.info("Updating session time ["
								+ socket.getPort() + "]");
						Managing.undateSessionLastRequest(gotKey);
						/* Если ключ есть в сессиях, пытаемся удалить письмо */
						logger.info("Trying to delete message ["
								+ socket.getPort() + "]");
						boolean deleted = Managing.deleteMess(
								(SimpleMessage) requestInfo[0],
								(String) requestInfo[1],
								Managing.getSessionUser(gotKey));
						if (deleted) {
							logger.info("Message is deleted ["
									+ socket.getPort() + "]");
							out.writeByte(0);
						} else {
							logger.info("Message is not deleted ["
									+ socket.getPort() + "]");
							out.writeByte(2);
						}
					}
				}
				/* Запрос на создание новой папки */
				if (requestNumber == 7) {
					logger.info("Request to create new folder ["
							+ socket.getPort() + "]");
					logger.info("Trying to get key and name of folder ["
							+ socket.getPort() + "]");
					int gotKey = in.readInt();
					int length = in.readInt();
					/* Получаем название папки */
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					String requestInfo = (String) Serializer
							.deserialize(requestBytes);
					logger.info("Got key and folder name [" + socket.getPort()
							+ "]");
					if (!Managing.sessionExists(gotKey)) {
						logger.info("Key does not exist [" + socket.getPort()
								+ "]");
						out.writeByte(3);
					} else {
						logger.info("Updating session time ["
								+ socket.getPort() + "]");
						Managing.undateSessionLastRequest(gotKey);
						/*
						 * Если ключ есть в сессиях, проверяем корректность
						 * названия
						 */
						logger.info("Checking folder name [" + socket.getPort()
								+ "]");
						boolean correctFolder = new ActionValidation()
								.checkFolderName(requestInfo);
						if (!correctFolder) {
							logger.info("Name of folder is not correct ["
									+ socket.getPort() + "]");
							out.writeByte(1);
						} else {
							logger.info("Trying to create folder ["
									+ socket.getPort() + "]");
							/* Если название корректно, пытаемся создать папку */
							SimpleFolder created = Managing.createFolder(
									requestInfo,
									Managing.getSessionUser(gotKey));
							if (created != null) {
								logger.info("Trying to send folder to user ["
										+ socket.getPort() + "]");
								/* Если папка создана, отправляем ее */
								byte[] answerBytes = Serializer
										.serialize(created);
								out.writeByte(0);
								out.writeInt(answerBytes.length);
								out.write(answerBytes);
							} else {
								logger.info("Folder is not created ["
										+ socket.getPort() + "]");
								out.writeByte(4);
							}
						}
					}
				}
				/* Запрос на удаление папки */
				if (requestNumber == 8) {
					logger.info("Request to delete folder [" + socket.getPort()
							+ "]");
					logger.info("Trying to get key and folder ["
							+ socket.getPort() + "]");
					int gotKey = in.readInt();
					int length = in.readInt();
					/* Получаем папку */
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					SimpleFolder requestInfo = (SimpleFolder) Serializer
							.deserialize(requestBytes);
					logger.info("Got folder and key [" + socket.getPort() + "]");
					if (!Managing.sessionExists(gotKey)) {
						logger.info("Key does not exist [" + socket.getPort()
								+ "]");
						out.writeByte(3);
					} else {
						logger.info("Updating session time ["
								+ socket.getPort() + "]");
						Managing.undateSessionLastRequest(gotKey);
						/* Если ключ есть в сессиях, пробуем удалить папку */
						logger.info("Trying to delete folder ["
								+ socket.getPort() + "]");
						byte deleted = Managing.deleteFolder(requestInfo,
								Managing.getSessionUser(gotKey));
						if (deleted == 1) {
							logger.info("Could not delete this folder ["
									+ socket.getPort() + "]");
							out.writeByte(1);
						} else if (deleted == 2) {
							logger.info("Could not delete folder ["
									+ socket.getPort() + "]");
							out.writeByte(2);
						} else if (deleted == 0) {
							logger.info("Folder is deleted ["
									+ socket.getPort() + "]");
							out.writeByte(0);
						}
					}
				}
				/* Запрос на перемещение письма */
				if (requestNumber == 9) {
					logger.info("Request to move message [" + socket.getPort()
							+ "]");
					logger.info("Trying to get key and data ["
							+ socket.getPort() + "]");
					int gotKey = in.readInt();
					int length = in.readInt();
					/* Получаем данные о письме */
					byte[] requestBytes = new byte[length];
					for (int i = 0; i < length; i++)
						requestBytes[i] = in.readByte();
					Object[] requestInfo = (Object[]) Serializer
							.deserialize(requestBytes);
					logger.info("Got key and data [" + socket.getPort() + "]");
					if (!Managing.sessionExists(gotKey)) {
						logger.info("Key does not exist [" + socket.getPort()
								+ "]");
						out.writeByte(3);
					} else {
						logger.info("Updating session time ["
								+ socket.getPort() + "]");
						Managing.undateSessionLastRequest(gotKey);
						/* Если ключ есть в сессиях, пробуем переместить письмо */
						SimpleMessage message = (SimpleMessage) requestInfo[0];
						String folder = (String) requestInfo[1];
						String moveFolder = (String) requestInfo[2];

						if (folder.equals(moveFolder)) {
							logger.info("Out folder equals target folder ["
									+ socket.getPort() + "]");
							out.writeByte(1);
						} else {
							logger.info("Trying to move message ["
									+ socket.getPort() + "]");
							boolean moved = Managing.moveMessage(message,
									folder, moveFolder,
									Managing.getSessionUser(gotKey));
							if (moved) {
								logger.info("Message is moved ["
										+ socket.getPort() + "]");
								out.writeByte(0);
							} else {
								logger.info("Message is not moved ["
										+ socket.getPort() + "]");
								out.writeByte(2);
							}
						}
					}
				}
				/* Запрос на проверку новых сообещений */
				if (requestNumber == 10) {
					logger.info("Request to check new messages ["
							+ socket.getPort() + "]");
					logger.info("Trying to get key and amount of messages ["
							+ socket.getPort() + "]");
					int gotKey = in.readInt();
					/* Получаем количество сообщений у пользователя */
					int amount = in.readInt();
					logger.info("Got key and amount of messages ["
							+ socket.getPort() + "]");
					if (!Managing.sessionExists(gotKey)) {
						logger.info("Key does not exist [" + socket.getPort()
								+ "]");
						out.writeByte(3);
					} else {
						logger.info("Checking if there are new messages ["
								+ socket.getPort() + "]");
						/*
						 * Если ключ есть в сессиях, проверяем есть ли новые
						 * сообщения
						 */
						int amountOfNew = Managing.hasNewMessages(amount,
								Managing.getSessionUser(gotKey));
						if (amountOfNew == -1) {
							logger.info("Could not check [" + socket.getPort()
									+ "]");
							out.writeByte(2);
						} else {
							/*
							 * Если есть новые, получаем их и отправляем
							 * пользователю
							 */
							if (amountOfNew == 0) {
								logger.info("There are not new messages ["
										+ socket.getPort() + "]");
								out.writeByte(1);
							} else {
								logger.info("Trying to get messages ["
										+ socket.getPort() + "]");
								List<SimpleMessage> newMessages = Managing
										.getNewMessages(amountOfNew,
												Managing.getSessionUser(gotKey));
								if (newMessages == null) {
									logger.info("Could not get messages ["
											+ socket.getPort() + "]");
									out.writeByte(2);
								} else {
									logger.info("Trying to send new messages to user ["
											+ socket.getPort() + "]");
									byte[] answerBytes = Serializer
											.serialize(newMessages);
									out.writeByte(0);
									out.writeInt(answerBytes.length);
									out.write(answerBytes);
									logger.info("Sent messages ["
											+ socket.getPort() + "]");
								}
							}
						}
					}
				}

				/* Запрос на очистку корзины */
				if (requestNumber == 11) {
					logger.info("Request to clear trach basket ["
							+ socket.getPort() + "]");
					logger.info("Trying to get key [" + socket.getPort() + "]");
					/* Проверяем, есть ли такое ключ в сессиях */
					int gotKey = in.readInt();
					if (!Managing.sessionExists(gotKey)) {
						logger.info("Key does not exist [" + socket.getPort()
								+ "]");
						out.writeByte(3);
					} else {
						logger.info("Updating session time ["
								+ socket.getPort() + "]");
						Managing.undateSessionLastRequest(gotKey);
						logger.info("Trying to clear trach basket ["
								+ socket.getPort() + "]");
						/* Если есть, пытаемся получить из БД данные */
						boolean answer = Managing.clearTrash(Managing
								.getSessionUser(gotKey));
						if (!answer) {
							logger.info("Could not clear trash basket ["
									+ socket.getPort() + "]");
							out.writeByte(2);
						} else {
							logger.info("Trash basket is cleared ["
									+ socket.getPort() + "]");
							out.writeByte(0);
						}
					}

				}
				/* Запрос на выход из системы */
				if (requestNumber == 0) {
					logger.info("Request to log out [" + socket.getPort() + "]");
					int gotKey = in.readInt();
					if (Managing.sessionExists(gotKey)) {
						Managing.deleteSession(gotKey);
						logger.info("Key removed [" + socket.getPort() + "]");
					}
				}
			} catch (IOException io) {
				logger.error("IO", io);
				io.printStackTrace();
			} catch (ClassNotFoundException cnf) {
				logger.error("ClassNotFound", cnf);
				cnf.printStackTrace();
			} finally {
				try {
					in.close();
					out.close();
					socket.close();
					logger.info(socket.getPort() + " - closed");
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/* Поток для работы с сессиями */
	private class SessionChecker extends Thread {
		SessionChecker() {
			super();
			setDaemon(true);
		}

		public void run() {
			List<Session> sessions = null;
			while (true) {
				/* Ждем */
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					logger.error("Interrapted session checker", e);
					e.printStackTrace();
				}
				/* Берем все сессии */
				sessions = Managing.getSessions();
				if (sessions != null) {
					long currentDate = System.currentTimeMillis();
					long sessionDate = 0;
					logger.info("Trying to get sessions");
					/* Просматриваем все сессии */
					for (Session session : sessions) {
						sessionDate = session.getTime().getTime();
						if (currentDate - sessionDate > 1000 * 60 * 5) {
							/* Удаляем неактивного пользователя */
							logger.info("Session for [" + session.getMail()
									+ "] is deleted");
							Managing.deleteSession(session.getId());
						}
					}
				}
			}
		}
	}
}