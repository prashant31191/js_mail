package org.wi;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.cc.Serializer;
import org.cc.SimpleFolder;
import org.cc.SimpleMessage;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main window for dealing with mail-server
 * 
 * @author Fomin
 * @version 1.0
 */
public class MainWindow extends JFrame {
	private final Logger logger = Logger.getLogger("MainWindow");

	private JPanel contentPane;
	private JLabel lblError;
	private JTextArea taMessText;

	private NewMessageChecker messageChecker;
	private Lock lock = new ReentrantLock();
	private int key;

	private SimpleDateFormat format = new SimpleDateFormat(
			"dd.MM.yyyy hh:mm:ss");

	private JList<String> lstFolders;
	private JList<String> lstMessages;

	private List<SimpleFolder> folders = null;
	private List<SimpleMessage> messages = null;
	private DefaultListModel<String> listModelMessages = new DefaultListModel<String>();
	private DefaultListModel<String> listModelFolders = new DefaultListModel<String>();

	@SuppressWarnings("unchecked")
	public MainWindow(final int key, String addressForField) {
		logger.setLevel(Level.INFO);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 731, 594);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		this.key = key;

		Socket socket = null;
		DataOutputStream out = null;
		DataInputStream in = null;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				/* Нажатие на закрытые окна */
				lock.lock();
				logger.info("Close window");
				Socket socket = null;
				DataOutputStream out = null;
				try {
					socket = new Socket("localhost", 6789);
				} catch (UnknownHostException e2) {
					logger.error("Unknown host", e2);
					e2.printStackTrace();
				} catch (IOException e2) {
					logger.error("IO", e2);
					e2.printStackTrace();
				}

				try {
					out = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e2) {
					logger.error("Stream error", e2);
					e2.printStackTrace();
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				try {
					logger.info("Sending key");
					/* Отпраляем запрос на выход из системы */
					messageChecker.setWorking(false);
					out.writeByte(0);
					out.writeInt(key);
					logger.info("Sent key");
				} catch (IOException ex) {
					logger.error("IO error", ex);
					ex.printStackTrace();
				} finally {
					lock.unlock();
					try {
						out.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		taMessText = new JTextArea();
		taMessText.setEditable(false);
		JScrollPane spMessageDetailed = new JScrollPane(taMessText);
		spMessageDetailed
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spMessageDetailed.setBounds(10, 258, 702, 269);
		contentPane.add(spMessageDetailed);

		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setBounds(10, 527, 301, 14);
		contentPane.add(lblError);

		lstFolders = new JList<String>(listModelFolders);
		lstFolders.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				taMessText.setText("");
				drawMessages();
			}
		});
		JScrollPane spFolders = new JScrollPane(lstFolders);
		spFolders
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spFolders.setBounds(10, 38, 224, 167);
		contentPane.add(spFolders);

		lstMessages = new JList<String>(listModelMessages);
		lstMessages.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/* Клик по сообщению */
				logger.info("Click on message");
				lock.lock();
				int selectedFolder = lstFolders.getSelectedIndex();
				if (selectedFolder > -1) {
					int selectedMessage = lstMessages.getSelectedIndex();
					if (selectedMessage > -1) {
						SimpleMessage message = folders.get(selectedFolder)
								.getMessages().get(selectedMessage);
						/* Формируем сообщение для отображения пользователю */
						StringBuilder sb = new StringBuilder();
						sb.append(format.format(message.getDate()) + "\n");
						sb.append("От: " + message.getFrom() + "\n");
						sb.append("Кому: " + message.getTo() + "\n");
						sb.append("Тема: " + message.getAbout() + "\n\n");
						sb.append(message.getText());
						taMessText.setText(sb.toString());

						if (!message.isRead()) {
							logger.info("Trying to send message");
							/*
							 * Если письмо не прочитаено, отправляем запрос на
							 * отметку как прочитанное
							 */
							Socket socket = null;
							DataOutputStream out = null;
							DataInputStream in = null;

							try {
								socket = new Socket("localhost", 6789);
							} catch (UnknownHostException e2) {
								logger.error("Unknown host", e2);
								e2.printStackTrace();
							} catch (IOException e2) {
								logger.error("IO", e2);
								e2.printStackTrace();
							}

							try {
								out = new DataOutputStream(socket
										.getOutputStream());
								in = new DataInputStream(socket
										.getInputStream());
							} catch (IOException e2) {
								logger.error("Stream error", e2);
								e2.printStackTrace();
								try {
									out.close();
									in.close();
									socket.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}

							try {
								byte[] requestBytes = Serializer
										.serialize(message);
								out.writeByte(5);
								out.writeInt(key);
								out.writeInt(requestBytes.length);
								out.write(requestBytes);
								logger.info("Sent message");
								/* Ждем ответ */
								byte answer = in.readByte();
								logger.info("Got answer");
								if (answer == 0) {
									logger.info("Setting message as read");
									/*
									 * Если отметилось, устанавливаем как
									 * прочитанное локально
									 */
									message.setRead(true);
									String read = "   ";
									String direction = null;
									String address = null;
									if (message.getFrom().equals("Меня")) {
										direction = " \u2192";
										address = message.getTo() + " ";
									} else {
										direction = " \u2190";
										address = message.getFrom() + " ";
									}
									String date = format.format(message
											.getDate()) + " ";
									String about = message.getAbout();
									String messAppearance = read + direction
											+ date + address + about;
									listModelMessages.remove(selectedMessage);
									listModelMessages.add(selectedMessage,
											messAppearance);
									lstMessages
											.setSelectedIndex(selectedMessage);
								} else if (answer == 3) {
									logger.info("Wrong key. Closing window");
									/* Выходим из основного окна */
									messageChecker.setWorking(false);
									Registration registration = new Registration();
									registration.setVisible(true);
									MainWindow.this.dispose();
								}
							} catch (IOException er) {
								logger.error("IO", er);
								er.printStackTrace();
							} finally {
								try {
									in.close();
									out.close();
									socket.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
						lock.unlock();
					}
				}
			}
		});
		JScrollPane spMessages = new JScrollPane(lstMessages);
		spMessages
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spMessages.setBounds(265, 38, 447, 167);
		contentPane.add(spMessages);

		JLabel label = new JLabel("Папки");
		label.setBounds(10, 22, 46, 14);
		contentPane.add(label);

		JLabel label_1 = new JLabel("Письма");
		label_1.setBounds(265, 22, 46, 14);
		contentPane.add(label_1);

		JButton btnDeleteFolder = new JButton("Удалить");
		btnDeleteFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* Удаление папки */
				logger.info("Deleting folder");
				lock.lock();
				int selectedFolder = lstFolders.getSelectedIndex();
				/* Смотрим, выделена ли папка */
				if (selectedFolder < 0)
					return;
				SimpleFolder sFolder = folders.get(selectedFolder);

				Socket socket = null;
				DataOutputStream out = null;
				DataInputStream in = null;

				try {
					socket = new Socket("localhost", 6789);
				} catch (UnknownHostException e2) {
					logger.error("Unknown host", e2);
					e2.printStackTrace();
				} catch (IOException e2) {
					logger.error("IO", e2);
					e2.printStackTrace();
				}

				try {
					out = new DataOutputStream(socket.getOutputStream());
					in = new DataInputStream(socket.getInputStream());
				} catch (IOException e2) {
					logger.error("Stream error", e2);
					e2.printStackTrace();
					try {
						out.close();
						in.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				try {
					logger.info("Trying to send request for deleting folder");
					/* Отправляем запрос на удаление */
					byte[] requestBytes = Serializer.serialize(sFolder);
					out.writeByte(8);
					out.writeInt(key);
					out.writeInt(requestBytes.length);
					out.write(requestBytes);
					logger.info("Sent request");
					byte answer = in.readByte();
					logger.info("Got answer");
					if (answer == 0) {
						logger.info("Trying to delete folder");
						/* Если папка удалена, удаляем ее локально */
						folders.remove(selectedFolder);
						listModelFolders.remove(selectedFolder);
						listModelMessages.removeAllElements();
						lblError.setText("");
						logger.info("Folder is deleted");
					} else if (answer == 1) {
						logger.info("Folder is not deleted");
						lblError.setText("Эту папку нельзя удалить");
					} else if (answer == 2) {
						logger.info("Server problems");
						lblError.setText("Проблемы с сервером");
					} else if (answer == 3) {
						logger.info("Wrong key. Closing window");
						/* Выходим из основного окна */
						messageChecker.setWorking(false);
						Registration registration = new Registration();
						registration.setVisible(true);
						MainWindow.this.dispose();
					}
				} catch (IOException er) {
					logger.error("IO", er);
					er.printStackTrace();
				} finally {
					lock.unlock();
					try {
						in.close();
						out.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnDeleteFolder.setBounds(143, 216, 91, 23);
		contentPane.add(btnDeleteFolder);

		JButton btnCreateFolder = new JButton("Создать");
		btnCreateFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Opening window Create folder");
				/* Вызываем окно для создания папки */
				CreateFold sm = new CreateFold();
				sm.setVisible(true);
			}
		});
		btnCreateFolder.setBounds(42, 216, 91, 23);
		contentPane.add(btnCreateFolder);

		JButton btnDelMess = new JButton("Удалить");
		btnDelMess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Prepearing delete message");
				/* Удаление сообщения */
				lock.lock();
				int selectedMessage = lstMessages.getSelectedIndex();
				/* Смотрим, выделено ли сообщение */
				if (selectedMessage < 0)
					return;

				int selectedFolder = lstFolders.getSelectedIndex();
				SimpleMessage message = folders.get(selectedFolder)
						.getMessages().get(selectedMessage);
				String folderName = listModelFolders.elementAt(selectedFolder);

				Socket socket = null;
				DataOutputStream out = null;
				DataInputStream in = null;

				try {
					socket = new Socket("localhost", 6789);
				} catch (UnknownHostException e2) {
					logger.error("Unknown host", e2);
					e2.printStackTrace();
				} catch (IOException e2) {
					logger.error("IO", e2);
					e2.printStackTrace();
				}

				try {
					out = new DataOutputStream(socket.getOutputStream());
					in = new DataInputStream(socket.getInputStream());
				} catch (IOException e2) {
					logger.error("Stream error", e2);
					e2.printStackTrace();
					try {
						out.close();
						in.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				try {
					if (folderName.equals("Корзина")) {
						logger.info("Sending request to delete message");
						/*
						 * Если выделенная папка - Корзина, отправляем запрос на
						 * удаление
						 */
						Object[] request = new Object[2];
						request[0] = message;
						request[1] = folderName;
						byte[] requestBytes = Serializer.serialize(request);
						out.writeByte(6);
						out.writeInt(key);
						out.writeInt(requestBytes.length);
						out.write(requestBytes);
						logger.info("Sent data");
						/* Ждем ответ */
						byte answer = in.readByte();
						logger.info("Got answer");
						if (answer == 0) {
							logger.info("Trying to delete message");
							/* Если успешно, удаляем письмо локально */
							folders.get(selectedFolder).getMessages()
									.remove(selectedMessage);
							listModelMessages.remove(selectedMessage);
							taMessText.setText("");
							logger.info("Message is deleted");
						} else if (answer == 3) {
							logger.info("Wrong key. Closing window");
							/* Выходим из основного окна */
							messageChecker.setWorking(false);
							Registration registration = new Registration();
							registration.setVisible(true);
							MainWindow.this.dispose();
						} else {
							logger.info("Server problems");
						}
					} else {
						logger.info("Trying to move message to trash basket");
						/*
						 * Если выделенная папка не Корзина, отправляем запрос
						 * на перемещение в Корзину
						 */
						Object[] request = new Object[3];
						request[0] = message;
						request[1] = folderName;
						request[2] = "Корзина";
						byte[] requestBytes = Serializer.serialize(request);
						out.writeByte(9);
						out.writeInt(key);
						out.writeInt(requestBytes.length);
						out.write(requestBytes);
						logger.info("Sent data");
						/* Ждем ответ */
						byte answer = in.readByte();
						logger.info("Got answer");
						if (answer == 0) {
							logger.info("Trying to move message to trash basket");
							/*
							 * Если успешно, перемещяем письмо в корзину
							 * локально
							 */
							int trash = listModelFolders.indexOf("Корзина");
							listModelMessages.remove(selectedMessage);
							folders.get(selectedFolder).getMessages()
									.remove(selectedMessage);
							folders.get(trash).addMessage(message);
							logger.info("Message is moved");
						} else if (answer == 1) {
							logger.info("Couldn't delete message");
							lblError.setText("Невозможно удалить");
						} else if (answer == 2) {
							logger.info("Server problems");
							lblError.setText("Проблемы с сервером");
						} else if (answer == 3) {
							logger.info("Wrong key. Closing window");
							/* Выходим из основного окна */
							messageChecker.setWorking(false);
							Registration registration = new Registration();
							registration.setVisible(true);
							MainWindow.this.dispose();
						}
					}
				} catch (IOException er) {
					logger.error("IO", er);
					er.printStackTrace();
				} finally {
					lock.unlock();
					try {
						in.close();
						out.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnDelMess.setBounds(621, 216, 91, 23);
		contentPane.add(btnDelMess);

		JButton btnCreatMess = new JButton("Написать");
		btnCreatMess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Opening window to send message");
				/* Вызываем окно для отправки сообщения */
				SendMessage sm = new SendMessage("");
				sm.setVisible(true);
			}
		});
		btnCreatMess.setBounds(520, 216, 91, 23);
		contentPane.add(btnCreatMess);

		JButton btnMoveMess = new JButton("Переместить");
		btnMoveMess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* Перемещение сообщения из папки в папку */
				int selectedMessage = lstMessages.getSelectedIndex();
				/* Проверяем выделено ли сообщение */
				if (selectedMessage < 0)
					return;
				logger.info("Opening window Move message");
				/* Вызываем окно для перемещения сообщения */
				MoveFold move = new MoveFold(lstFolders.getSelectedIndex(),
						selectedMessage);
				move.setVisible(true);
			}
		});
		btnMoveMess.setBounds(383, 216, 127, 23);
		contentPane.add(btnMoveMess);

		JButton btnLogOut = new JButton("Выйти");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Logging out");
				/* Выход из системы */
				lock.lock();
				Socket socket = null;
				DataOutputStream out = null;
				try {
					socket = new Socket("localhost", 6789);
				} catch (UnknownHostException e2) {
					logger.error("Unknown host", e2);
					e2.printStackTrace();
				} catch (IOException e2) {
					logger.error("IO", e2);
					e2.printStackTrace();
				}

				try {
					out = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e2) {
					logger.error("Stream error", e2);
					e2.printStackTrace();
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				try {
					logger.info("Sending request to log out");
					/* Отправляем запрос на выход */
					logger.info("Stopping New message checker");
					messageChecker.setWorking(false);
					out.writeByte(0);
					out.writeInt(key);

					logger.info("Closing window");
					/* Выходим из основного окна */
					Registration registration = new Registration();
					registration.setVisible(true);
					MainWindow.this.dispose();
				} catch (IOException ex) {
					logger.error("IO error", ex);
					ex.printStackTrace();
				} finally {
					lock.unlock();
					try {
						out.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnLogOut.setBounds(623, 8, 91, 23);
		contentPane.add(btnLogOut);

		JLabel lblAddress = new JLabel("");
		lblAddress.setBounds(10, 0, 224, 14);
		lblAddress.setText(addressForField);
		contentPane.add(lblAddress);

		JButton button = new JButton("Очистить корзину");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Request to clear trash basket");
				/* Запрос на очистку корзины */
				lock.lock();
				int trashIndex = listModelFolders.indexOf("Корзина");
				int numberOfMessages = folders.get(trashIndex).getMessages()
						.size();
				/* Смотрим, сколько писем в корзине */
				if (numberOfMessages < 1)
					return;

				Socket socket = null;
				DataOutputStream out = null;
				DataInputStream in = null;

				try {
					socket = new Socket("localhost", 6789);
				} catch (UnknownHostException e2) {
					logger.error("Unknown host", e2);
					e2.printStackTrace();
				} catch (IOException e2) {
					logger.error("IO", e2);
					e2.printStackTrace();
				}

				try {
					out = new DataOutputStream(socket.getOutputStream());
					in = new DataInputStream(socket.getInputStream());
				} catch (IOException e2) {
					logger.error("Stream error", e2);
					e2.printStackTrace();
					try {
						out.close();
						in.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				try {
					logger.info("Senfing key");
					/* Отправляем запрос на очистку корзины */
					out.writeByte(11);
					out.writeInt(key);
					logger.info("Sent key");
					/* Ждем ответ */
					byte answer = in.readByte();
					logger.info("Got answer");
					if (answer == 0) {
						logger.info("Trying to clear trash basket");
						/* Если успешно, удаляем письма из корзины локально */
						folders.get(trashIndex).getMessages().clear();
						if (lstFolders.getSelectedIndex() == trashIndex) {
							listModelMessages.clear();
							taMessText.setText("");
						}
						logger.info("Trash basket cleared");
					} else if (answer == 3) {
						logger.info("Wrong key. Closing window");
						/* Выходим из основного окна */
						messageChecker.setWorking(false);
						Registration registration = new Registration();
						registration.setVisible(true);
						MainWindow.this.dispose();
					} else {
						logger.info("Server problems");
						lblError.setText("Проблемы с сервером");
					}
				} catch (IOException er) {
					logger.error("IO", er);
					er.printStackTrace();
				} finally {
					lock.unlock();
					try {
						in.close();
						out.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		button.setBounds(452, 8, 162, 23);
		contentPane.add(button);

		JButton button_1 = new JButton("Ответить");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedMessage = lstMessages.getSelectedIndex();
				if (selectedMessage < 0) {
					return;
				}

				int selectedFolder = lstFolders.getSelectedIndex();
				SimpleMessage message = folders.get(selectedFolder)
						.getMessages().get(selectedMessage);
				if (!message.getTo().equals("Мне")) {
					return;
				}
				
				logger.info("Opening window Send message to response");
				/* Вызываем окно для отправки сообщения */
				SendMessage sm = new SendMessage(message.getFrom());
				sm.setVisible(true);
			}
		});
		button_1.setBounds(621, 538, 91, 23);
		contentPane.add(button_1);

		lock.lock();
		try {
			socket = new Socket("localhost", 6789);
		} catch (UnknownHostException e2) {
			logger.error("Unknown host", e2);
			e2.printStackTrace();
		} catch (IOException e2) {
			logger.error("IO", e2);
			e2.printStackTrace();
		}

		try {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e2) {
			logger.error("Stream error", e2);
			e2.printStackTrace();
			try {
				out.close();
				in.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		try {
			logger.info("Sending request to get everything");
			/* Отправляем запрос на получение всех папок и писем */
			out.writeByte(3);
			out.writeInt(key);
			logger.info("Sent key");
			/* Ждем ответ */
			byte answer = in.readByte();
			logger.info("Got answer");
			if (answer == 3) {
				logger.info("Wrong key. Close window");
				/* Выходим из основного окна */
				messageChecker.setWorking(false);
				Registration registration = new Registration();
				registration.setVisible(true);
				MainWindow.this.dispose();
			} else if (answer == 2) {
				logger.info("Server problems");
				taMessText.setText("Проблемы с сервером");
			} else {
				logger.info("Trying to get folders and messages");
				/* Если успешно, получаем папки с сообщениями */
				int length = in.readInt();
				byte[] answerBytes = new byte[length];
				for (int i = 0; i < length; i++)
					answerBytes[i] = in.readByte();
				folders = (List<SimpleFolder>) Serializer
						.deserialize(answerBytes);
				logger.info("Got folders and messages");
			}
		} catch (IOException er) {
			logger.error("IO", er);
			er.printStackTrace();
		} catch (ClassNotFoundException er) {
			er.printStackTrace();
		} finally {
			lock.unlock();
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		/* Выводим папки пользователю */
		for (SimpleFolder sf : folders) {
			listModelFolders.add(folders.indexOf(sf), sf.getName());
		}
		logger.info("Creating new message checker");
		/* Запускаем поток для проверки новых сообщений */
		messageChecker = new NewMessageChecker();
		messageChecker.setDaemon(true);
		messageChecker.start();
	}

	private void drawMessages() {
		logger.info("Drawing messages");
		/* Прописовка сообщений при нажатии на папку */
		int index = lstFolders.getSelectedIndex();
		if (index > -1) {
			/* Удаляем с экрана текущие сообщения */
			listModelMessages.removeAllElements();
			SimpleFolder folder = folders.get(index);
			List<SimpleMessage> messages = folder.getMessages();
			/* Формируем и прорысовывем новые */
			for (SimpleMessage message : messages) {
				String read = message.isRead() ? "   " : " \u00B7 ";
				String direction = null;
				String address = null;
				if (message.getFrom().equals("Меня")) {
					direction = " \u2192";
					address = message.getTo() + " ";
				} else {
					direction = " \u2190";
					address = message.getFrom() + " ";
				}
				String date = format.format(message.getDate()) + " ";
				String about = message.getAbout();
				String messAppearance = read + direction + date + address
						+ about;
				listModelMessages
						.add(messages.indexOf(message), messAppearance);
			}
		}
	}

	private class SendMessage extends JFrame {

		private JPanel contentPane;
		private JTextField fldAbout;
		private JTextField fldTo;

		public SendMessage(String to) {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 450, 358);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);

			JLabel label = new JLabel("От кого:");
			label.setBounds(10, 11, 46, 14);
			contentPane.add(label);

			JLabel label_1 = new JLabel("Кому:");
			label_1.setBounds(10, 50, 46, 14);
			contentPane.add(label_1);

			JLabel label_2 = new JLabel("Тема:");
			label_2.setBounds(10, 89, 46, 14);
			contentPane.add(label_2);

			JLabel label_3 = new JLabel("Сообщение:");
			label_3.setBounds(10, 128, 67, 14);
			contentPane.add(label_3);

			final JTextArea taMessage = new JTextArea();
			taMessage.setLineWrap(true);
			JScrollPane sp = new JScrollPane(taMessage);
			sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp.setBounds(10, 153, 422, 133);
			contentPane.add(sp);

			final JLabel lblErrorMess = new JLabel("");
			lblErrorMess.setForeground(Color.RED);
			lblErrorMess.setBounds(10, 288, 223, 14);
			contentPane.add(lblErrorMess);

			final JLabel lblErrorAbout = new JLabel("");
			lblErrorAbout.setForeground(Color.RED);
			lblErrorAbout.setBounds(83, 107, 284, 14);
			contentPane.add(lblErrorAbout);

			final JLabel lblErrorTo = new JLabel("");
			lblErrorTo.setForeground(Color.RED);
			lblErrorTo.setBounds(83, 67, 297, 14);
			contentPane.add(lblErrorTo);

			JButton btnSend = new JButton("Отправить");
			btnSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logger.info("Request to send mew message");
					/* Отправляем сообщение */
					lock.lock();
					Socket socket = null;
					DataOutputStream out = null;
					DataInputStream in = null;

					try {
						socket = new Socket("localhost", 6789);
					} catch (UnknownHostException e2) {
						logger.error("Unknown host", e2);
						e2.printStackTrace();
					} catch (IOException e2) {
						logger.error("IO", e2);
						e2.printStackTrace();
					}

					try {
						out = new DataOutputStream(socket.getOutputStream());
						in = new DataInputStream(socket.getInputStream());
					} catch (IOException e2) {
						logger.error("Stream error", e2);
						e2.printStackTrace();
						try {
							out.close();
							in.close();
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

					try {
						logger.info("Creating data and trying to send to server");
						/* Отправляем запрос на отправлку сообщения */
						String[] requestInfo = {
								fldTo.getText().toLowerCase().trim(),
								fldAbout.getText().trim(),
								taMessage.getText().trim() };
						byte[] requestBytes = Serializer.serialize(requestInfo);
						out.writeByte(4);
						out.writeInt(key);
						out.writeInt(requestBytes.length);
						out.write(requestBytes);
						logger.info("Sent data");
						byte answer = in.readByte();
						logger.info("Got answer");
						if (answer == 0) {
							logger.info("Trying to get new messages");
							/* Если успешно */
							int length = in.readInt();
							byte[] answerBytes = new byte[length];
							for (int i = 0; i < length; i++)
								answerBytes[i] = in.readByte();
							Object[] messages = (Object[]) Serializer
									.deserialize(answerBytes);
							logger.info("Got messages");
							lblErrorAbout.setText("");
							lblErrorTo.setText("");
							lblErrorMess.setText("");

							SimpleFolder outFolder = null;
							SimpleFolder inFolder = null;
							/* Получаем локальные папки входящие и исходящие */
							for (SimpleFolder sf : folders) {
								if (sf.getName().equals("Исходящие"))
									outFolder = sf;
								if (sf.getName().equals("Входящие"))
									inFolder = sf;
							}
							logger.info("Adding messages");
							/* Добавляем сообщения в локальные папки */
							if (messages[0] != null)
								outFolder
										.addMessage((SimpleMessage) messages[0]);
							List<SimpleMessage> incomeMessages = (List<SimpleMessage>) messages[1];
							for (SimpleMessage sm : incomeMessages)
								inFolder.addMessage(sm);
							/* Прописовываем сообщения */
							drawMessages();
							/* Закрываем окно */
							SendMessage.this.dispose();
						} else if (answer == 1) {
							logger.info("Trying to get mask");
							/* Если неудачно, выводим информацию пользователю */
							int length = in.readInt();
							byte[] answerBytes = new byte[length];
							for (int i = 0; i < length; i++)
								answerBytes[i] = in.readByte();
							boolean[] data = (boolean[]) Serializer
									.deserialize(answerBytes);
							logger.info("Got mask");
							lblErrorAbout.setText("");
							lblErrorTo.setText("");
							lblErrorMess.setText("");
							logger.info("Writing errors");
							if (!data[1])
								lblErrorTo
										.setText("Некорректный адресс получателя");
							if (!data[2])
								lblErrorAbout
										.setText("Некорректное поле \"Тема\"");
							if (!data[3])
								lblErrorMess.setText("Пустое сообщение");

						} else if (answer == 2) {
							logger.info("Server problems");
							lblErrorAbout.setText("");
							lblErrorTo.setText("");
							lblErrorMess.setText("");

							lblErrorMess.setText("Проблемы с сервером");
						} else {
							logger.info("Wrong key. Closing window");
							/* Выходим из основного окна */
							messageChecker.setWorking(false);
							Registration registration = new Registration();
							registration.setVisible(true);
							SendMessage.this.dispose();
							MainWindow.this.dispose();
						}
					} catch (IOException ex) {
						logger.error("IO error", ex);
						ex.printStackTrace();
					} catch (ClassNotFoundException er) {
						logger.error("ClassNotFound", er);
						er.printStackTrace();
					} finally {
						lock.unlock();
						try {
							in.close();
							out.close();
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			btnSend.setBounds(341, 297, 91, 23);
			contentPane.add(btnSend);

			JButton btnClear = new JButton("Очистить");
			btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					taMessage.setText("");
				}
			});
			btnClear.setBounds(243, 297, 91, 23);
			contentPane.add(btnClear);

			fldAbout = new JTextField();
			fldAbout.setBounds(83, 86, 349, 20);
			contentPane.add(fldAbout);
			fldAbout.setColumns(10);

			fldTo = new JTextField();
			fldTo.setBounds(83, 47, 349, 20);
			contentPane.add(fldTo);
			fldTo.setText(to);
			fldTo.setColumns(10);

			JLabel lblMailmailjs = new JLabel("Меня");
			lblMailmailjs.setBounds(83, 11, 81, 14);
			contentPane.add(lblMailmailjs);
		}
	}

	private class CreateFold extends JFrame {

		private JPanel contentPane;

		private JTextField fldFoldName;

		public CreateFold() {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 270, 154);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);

			JLabel lblName = new JLabel("Имя папки");
			lblName.setBounds(10, 11, 114, 14);
			contentPane.add(lblName);

			fldFoldName = new JTextField();
			fldFoldName.setBounds(10, 26, 237, 20);
			contentPane.add(fldFoldName);
			fldFoldName.setColumns(10);

			final JLabel lblError = new JLabel("");
			lblError.setBounds(10, 49, 237, 14);
			lblError.setForeground(Color.RED);
			contentPane.add(lblError);

			JButton btnCreateFold = new JButton("Создать папку");
			btnCreateFold.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logger.info("Request to create folder");
					/* Создание папки */
					lock.lock();
					String requestInfo = fldFoldName.getText().trim();
					Socket socket = null;
					DataOutputStream out = null;
					DataInputStream in = null;

					try {
						socket = new Socket("localhost", 6789);
					} catch (UnknownHostException e2) {
						logger.error("Unknown host", e2);
						e2.printStackTrace();
					} catch (IOException e2) {
						logger.error("IO", e2);
						e2.printStackTrace();
					}

					try {
						out = new DataOutputStream(socket.getOutputStream());
						in = new DataInputStream(socket.getInputStream());
					} catch (IOException e2) {
						logger.error("Stream error", e2);
						e2.printStackTrace();
						try {
							out.close();
							in.close();
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

					try {
						logger.info("Sending request to create folder");
						/* Отправляем запрос на создание папки */
						byte[] requestBytes = Serializer.serialize(requestInfo);
						out.writeByte(7);
						out.writeInt(key);
						out.writeInt(requestBytes.length);
						out.write(requestBytes);
						logger.info("Sent request");
						/* Ждем ответ */
						byte answer = in.readByte();
						logger.info("Got answer");
						if (answer == 0) {
							logger.info("Getting folder");
							/* Если удачно, получаем папку */
							int length = in.readInt();
							byte[] answerBytes = new byte[length];
							for (int i = 0; i < length; i++)
								answerBytes[i] = in.readByte();
							SimpleFolder folder = (SimpleFolder) Serializer
									.deserialize(answerBytes);
							logger.info("Got folder");
							/* Добавляем ее локально */
							folders.add(folder);
							listModelFolders.add(listModelFolders.size(),
									folder.getName());
							/* Закрываем окно */
							CreateFold.this.dispose();
						} else if (answer == 1) {
							logger.info("Wrong name of folder");
							lblError.setText("Неверное название папки");
						} else if (answer == 2) {
							logger.info("Server problems");
							lblError.setText("Проблемы с сервером");
						} else if (answer == 3) {
							logger.info("Wrong key. CLosing window");
							/* Выходим из основного окна */
							messageChecker.setWorking(false);
							Registration registration = new Registration();
							registration.setVisible(true);
							CreateFold.this.dispose();
							MainWindow.this.dispose();
						} else {
							logger.info("Such a folder already exists");
							lblError.setText("Папка с таким именем уже есть");
						}
					} catch (IOException ex) {
						logger.error("IO error", ex);
						ex.printStackTrace();
					} catch (ClassNotFoundException er) {
						logger.error("ClassNotFound", er);
						er.printStackTrace();
					} finally {
						lock.unlock();
						try {
							in.close();
							out.close();
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			btnCreateFold.setBounds(127, 74, 120, 23);
			contentPane.add(btnCreateFold);
		}
	}

	private class MoveFold extends JFrame {

		private JPanel contentPane;
		private int selectedFolder;
		private int selectedMessage;

		JList<String> lstMoveFolders;
		private JLabel lblError;

		public MoveFold(final int selectedFolder, final int selectedMessage) {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 252, 212);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);

			this.selectedFolder = selectedFolder;
			this.selectedMessage = selectedMessage;

			JLabel label = new JLabel("Папки:");
			label.setBounds(10, 11, 46, 14);
			contentPane.add(label);

			lstMoveFolders = new JList<String>(listModelFolders);
			JScrollPane spFolders = new JScrollPane(lstMoveFolders);
			spFolders
					.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			spFolders.setBounds(10, 28, 224, 108);
			contentPane.add(spFolders);

			lblError = new JLabel("");
			lblError.setBounds(10, 136, 224, 14);
			contentPane.add(lblError);

			JButton btnMove = new JButton("Переместить");
			btnMove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logger.info("Request to move message");
					/* Перемещение письма */
					lock.lock();
					int selectedMoveFolder = lstMoveFolders.getSelectedIndex();
					/* Проверяем, выделена ли папка назначения */
					if (selectedMoveFolder < 0)
						return;
					Object[] requestInfo = new Object[3];
					SimpleMessage message = folders.get(selectedFolder)
							.getMessages().get(selectedMessage);
					String folder = folders.get(selectedFolder).getName();
					String moveFolder = folders.get(selectedMoveFolder)
							.getName();
					/* Формируем запрос */
					requestInfo[0] = message;
					requestInfo[1] = folder;
					requestInfo[2] = moveFolder;

					Socket socket = null;
					DataOutputStream out = null;
					DataInputStream in = null;

					try {
						socket = new Socket("localhost", 6789);
					} catch (UnknownHostException e2) {
						logger.error("Unknown host", e2);
						e2.printStackTrace();
					} catch (IOException e2) {
						logger.error("IO", e2);
						e2.printStackTrace();
					}

					try {
						out = new DataOutputStream(socket.getOutputStream());
						in = new DataInputStream(socket.getInputStream());
					} catch (IOException e2) {
						logger.error("Stream error", e2);
						e2.printStackTrace();
						try {
							out.close();
							in.close();
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

					try {
						logger.info("Sending request to move emssage");
						/* Отправляем запрос на перемещение */
						byte[] requestBytes = Serializer.serialize(requestInfo);
						out.writeByte(9);
						out.writeInt(key);
						out.writeInt(requestBytes.length);
						out.write(requestBytes);
						logger.info("Sent request");
						/* Ждем ответ */
						byte answer = in.readByte();
						logger.info("Got answer");
						if (answer == 0) {
							logger.info("Moving message");
							/* Если успешно, перемещаем письмо локально */
							String messageAppearance = listModelMessages
									.get(selectedMessage);
							listModelMessages.remove(selectedMessage);
							folders.get(selectedFolder).getMessages()
									.remove(selectedMessage);
							folders.get(selectedMoveFolder).addMessage(message);
							dispose();
							taMessText.setText("");
							logger.info("Message is moved");
						} else if (answer == 1) {
							logger.info("COuldn't move");
							lblError.setText("Невозможно переместить");
						} else if (answer == 2) {
							logger.info("Server problems");
							lblError.setText("Проблемы с сервером");
						} else if (answer == 3) {
							logger.info("Wrong key. Closing window");
							/* Выходим из основного окна */
							messageChecker.setWorking(false);
							Registration registration = new Registration();
							registration.setVisible(true);
							MoveFold.this.dispose();
							MainWindow.this.dispose();
						}
					} catch (IOException ex) {
						logger.error("IO error", ex);
						ex.printStackTrace();
					} finally {
						lock.unlock();
						try {
							in.close();
							out.close();
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			btnMove.setBounds(142, 151, 91, 23);
			contentPane.add(btnMove);
		}
	}

	private class NewMessageChecker extends Thread {
		private final Logger nmcLogger = Logger.getLogger("NewMessageChecker");

		Socket socket = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		private boolean working = true;

		public void setWorking(boolean working) {
			this.working = working;
			nmcLogger.info("New message checker. Set working: " + working);
		}

		public void run() {
			nmcLogger.setLevel(Level.INFO);
			logger.info("New message checker started");
			/* Находим папку Входящие */
			SimpleFolder inputFolder = null;
			for (SimpleFolder tmpFolder : folders) {
				if (tmpFolder.getName().equals("Входящие")) {
					nmcLogger.info("Folder input is found");
					inputFolder = tmpFolder;
					break;
				}
			}

			while (working) {
				try {
					nmcLogger.info("New message checker is going to turn in");
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					nmcLogger.error("Interrapted new message checker", e);
					e.printStackTrace();
				}
				lock.lock();
				try {
					socket = new Socket("localhost", 6789);
				} catch (UnknownHostException e2) {
					nmcLogger.error("Unknown host", e2);
					e2.printStackTrace();
				} catch (IOException e2) {
					nmcLogger.error("IO", e2);
					e2.printStackTrace();
				}

				try {
					out = new DataOutputStream(socket.getOutputStream());
					in = new DataInputStream(socket.getInputStream());
				} catch (IOException e2) {
					nmcLogger.error("Stream error", e2);
					e2.printStackTrace();
					try {
						out.close();
						in.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				try {
					nmcLogger.info("Sending request to check new messages");
					/* Отправляем запрос с количеством сообщений */
					out.writeByte(10);
					out.writeInt(key);
					out.writeInt(inputFolder.getMessages().size());
					nmcLogger.info("Sent request");
					/* Ждем ответ */
					byte answer = in.readByte();
					nmcLogger.info("Got answer");
					if (answer == 0) {
						nmcLogger.info("Getting messages");
						/* Если удачно, получаем сообщения и добавляем локально */
						int length = in.readInt();
						byte[] answerBytes = new byte[length];
						for (int i = 0; i < length; i++)
							answerBytes[i] = in.readByte();
						List<SimpleMessage> gotMessages = (List<SimpleMessage>) Serializer
								.deserialize(answerBytes);

						for (SimpleMessage sMessage : gotMessages)
							inputFolder.addMessage(sMessage);
						/* Выводим сообщения в выделенной папке */
						drawMessages();
					} else if (answer == 1) {
						nmcLogger.info("There are not new messages");
					} else if (answer == 2) {
						nmcLogger.info("Server problems");
						lblError.setText("Проблемы с сервером");
					} else if (answer == 3) {
						nmcLogger.info("Wrong key. Closing window");
						/* Выходим из основного окна */
						working = false;
						Registration registration = new Registration();
						registration.setVisible(true);
						MainWindow.this.dispose();
					}
				} catch (IOException ex) {
					nmcLogger.error("IO error", ex);
					ex.printStackTrace();
				} catch (ClassNotFoundException er) {
					nmcLogger.error("ClassNotFound", er);
					er.printStackTrace();
				} finally {
					lock.unlock();
					try {
						in.close();
						out.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
