package org.wi;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.cc.Serializer;

import java.awt.Color;

/**
 * Window to log in or sign up
 * 
 * @author Fomin
 * @version 1.0
 */
public class Registration extends JFrame {
	private static final Logger logger = Logger.getLogger("Registration");

	private JPanel contentPane;
	private JTextField fldEnterMail;
	private JPasswordField fldEnterPass;
	private JTextField fldLogin;
	private JPasswordField fldFirstPass;
	private JPasswordField fldSecPass;
	private JTextField fldFirstName;
	private JTextField fldSecName;
	private JTextField fldBirthDate;
	private JTextField fldPhone;

	private JLabel lblErrorPair;
	private JLabel lblErrorLogin;
	private JLabel lblErrorPass1;
	private JLabel lblErrorPass2;
	private JLabel lblErrorPasses;
	private JLabel lblErrorFName;
	private JLabel lblErrorSName;
	private JLabel lblErrorDate;
	private JLabel lblErrorNumber;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Registration frame = new Registration();
				frame.setVisible(true);
			}
		});
	}

	public Registration() {
		logger.setLevel(Level.INFO);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 631, 446);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblGfhjkm = new JLabel("Пароль:");
		lblGfhjkm.setBounds(344, 15, 53, 14);
		lblGfhjkm.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblGfhjkm);
		
		lblErrorPair = new JLabel("");
		lblErrorPair.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblErrorPair.setForeground(Color.RED);
		lblErrorPair.setBounds(236, 32, 367, 14);
		contentPane.add(lblErrorPair);
		
		lblErrorLogin = new JLabel("");
		lblErrorLogin.setForeground(Color.RED);
		lblErrorLogin.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblErrorLogin.setBounds(113, 107, 500, 14);
		contentPane.add(lblErrorLogin);
		
		lblErrorPass1 = new JLabel("");
		lblErrorPass1.setForeground(Color.RED);
		lblErrorPass1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblErrorPass1.setBounds(111, 152, 502, 14);
		contentPane.add(lblErrorPass1);
		
		lblErrorPass2 = new JLabel("");
		lblErrorPass2.setForeground(Color.RED);
		lblErrorPass2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblErrorPass2.setBounds(112, 187, 501, 14);
		contentPane.add(lblErrorPass2);
		
		lblErrorPasses = new JLabel("");
		lblErrorPasses.setForeground(Color.RED);
		lblErrorPasses.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblErrorPasses.setBounds(111, 202, 502, 14);
		contentPane.add(lblErrorPasses);
		
		lblErrorFName = new JLabel("");
		lblErrorFName.setForeground(Color.RED);
		lblErrorFName.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblErrorFName.setBounds(110, 246, 503, 14);
		contentPane.add(lblErrorFName);
		
		lblErrorSName = new JLabel("");
		lblErrorSName.setForeground(Color.RED);
		lblErrorSName.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblErrorSName.setBounds(109, 284, 504, 14);
		contentPane.add(lblErrorSName);
		
		lblErrorDate = new JLabel("");
		lblErrorDate.setForeground(Color.RED);
		lblErrorDate.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblErrorDate.setBounds(109, 318, 504, 14);
		contentPane.add(lblErrorDate);
		
		lblErrorNumber = new JLabel("");
		lblErrorNumber.setForeground(Color.RED);
		lblErrorNumber.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblErrorNumber.setBounds(110, 354, 503, 14);
		contentPane.add(lblErrorNumber);
		
		fldEnterMail = new JTextField();
		fldEnterMail.setBounds(234, 12, 100, 20);
		contentPane.add(fldEnterMail);
		fldEnterMail.setColumns(10);

		JLabel lblEmail = new JLabel("E-mail:");
		lblEmail.setBounds(191, 15, 46, 14);
		contentPane.add(lblEmail);

		fldEnterPass = new JPasswordField();
		fldEnterPass.setBounds(395, 12, 106, 20);
		contentPane.add(fldEnterPass);
		fldEnterPass.setColumns(10);

		JLabel label_1 = new JLabel("Регистрация");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		label_1.setBounds(10, 53, 132, 28);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("Логин:");
		label_2.setBounds(10, 92, 46, 14);
		contentPane.add(label_2);

		fldLogin = new JTextField();
		fldLogin.setBounds(121, 86, 126, 20);
		contentPane.add(fldLogin);
		fldLogin.setColumns(10);

		JLabel lblmailjs = new JLabel("@mail.js");
		lblmailjs.setBounds(249, 90, 58, 14);
		contentPane.add(lblmailjs);

		JLabel label_3 = new JLabel("Пароль:");
		label_3.setBounds(10, 135, 100, 14);
		contentPane.add(label_3);

		fldFirstPass = new JPasswordField();
		fldFirstPass.setBounds(121, 132, 126, 20);
		contentPane.add(fldFirstPass);
		fldFirstPass.setColumns(10);

		fldSecPass = new JPasswordField();
		fldSecPass.setBounds(121, 167, 126, 20);
		contentPane.add(fldSecPass);
		fldSecPass.setColumns(10);

		JLabel label_4 = new JLabel("Повторите пароль:");
		label_4.setBounds(10, 170, 115, 14);
		contentPane.add(label_4);

		fldFirstName = new JTextField();
		fldFirstName.setBounds(120, 227, 127, 20);
		contentPane.add(fldFirstName);
		fldFirstName.setColumns(10);

		fldSecName = new JTextField();
		fldSecName.setBounds(120, 264, 127, 20);
		contentPane.add(fldSecName);
		fldSecName.setColumns(10);

		fldBirthDate = new JTextField();
		fldBirthDate.setBounds(120, 299, 127, 20);
		contentPane.add(fldBirthDate);
		fldBirthDate.setColumns(10);

		fldPhone = new JTextField();
		fldPhone.setBounds(120, 333, 127, 20);
		contentPane.add(fldPhone);
		fldPhone.setColumns(10);

		JLabel label_5 = new JLabel("Имя:");
		label_5.setBounds(10, 230, 188, 14);
		contentPane.add(label_5);

		JLabel label_6 = new JLabel("Фамилия:");
		label_6.setBounds(10, 267, 188, 14);
		contentPane.add(label_6);

		JLabel label_7 = new JLabel("Дата рождения:");
		label_7.setBounds(10, 302, 188, 14);
		contentPane.add(label_7);

		JLabel label_8 = new JLabel("Телефон:");
		label_8.setBounds(10, 336, 188, 14);
		contentPane.add(label_8);

		JButton btnLogIn = new JButton("Войти");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearErrorLabels();
				logger.info("Requeat to log in");
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
				/* Отправляем запрос на вход в систему */
				try {
					logger.info("Sending data to log in");
					String[] requestInfo = {
							fldEnterMail.getText().toLowerCase().trim(),
							fldEnterPass.getText() };

					byte[] requestBytes = Serializer.serialize(requestInfo);
					out.writeByte(2);
					out.writeInt(requestBytes.length);
					out.write(requestBytes);
					logger.info("Sent data");
					/* Ждем ответ */
					byte answer = in.readByte();
					logger.info("Got answer");
					if (answer == 0) {
						logger.info("Creating Maing window");
						/* Если успешно, создаем окно с почтой */
						int key = in.readInt();
						MainWindow mw = new MainWindow(key, requestInfo[0]);
						mw.setVisible(true);
						Registration.this.dispose();
					} else if (answer == 2) {
						logger.info("Wrong pair");
						lblErrorPair.setText("Неверная пара E-mail / Пароль");
					} else {
						logger.info("Incorrect data");
						lblErrorPair.setText("Некорректные входные данные");
					}
				} catch (IOException ex) {
					logger.error("IO error", ex);
					ex.printStackTrace();
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
		});
		btnLogIn.setBounds(522, 11, 91, 23);
		contentPane.add(btnLogIn);

		JButton btnSignUp = new JButton("Зарег-я");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearErrorLabels();
				logger.info("Request for registration");
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
					logger.info("Sending request for registration");
					/* Отправляем запрос на регистрацию */
					String[] requestInfo = { fldLogin.getText().toLowerCase(),
							fldFirstPass.getText(), fldSecPass.getText(),
							fldFirstName.getText().trim(),
							fldSecName.getText().trim(),
							fldBirthDate.getText().trim(),
							fldPhone.getText().trim() };

					byte[] requestBytes = Serializer.serialize(requestInfo);
					out.writeByte(1);
					out.writeInt(requestBytes.length);
					out.write(requestBytes);
					logger.info("Sent data");
					byte answer = in.readByte();
					logger.info("Got answer");
					if (answer == 0) {
						logger.info("Creating Main Window");
						/* Если успешно, создаем окно с почной */
						int key = in.readInt();
						MainWindow mw = new MainWindow(key, requestInfo[0]
								+ "@mail.js");
						mw.setVisible(true);
						Registration.this.dispose();
					} else if (answer == 1) {
						logger.info("Reading mask");
						/* Если неуспешно, считываем маску */
						StringBuilder sb = new StringBuilder();
						int length = in.readInt();
						byte[] answerBytes = new byte[length];
						for (int i = 0; i < length; i++)
							answerBytes[i] = in.readByte();
						boolean[] answers = (boolean[]) Serializer
								.deserialize(answerBytes);
						logger.info("Printing errors");
						/* Показываем пользователю причины */
						if (!answers[1])
							lblErrorLogin.setText("Некоректный логин. "
									+ "(Минимальная длина 4 символа. Латинские буквы,"
									+ " \".\", \"-\", \"_\")");
						if (!answers[2])
							lblErrorPass1.setText("Некорректный пароль 1. "
									+ "(Минимальная длина 8 символов. Латинские буквы,"
									+ " \".\", \",\", \"-\", \"_\", \"%\", \"*\")");
						if (!answers[3])
							lblErrorPass2.setText("Некорректный пароль 2. "
									+ "(Минимальная длина 8 символов. Латинские буквы,"
									+ " \".\", \",\", \"-\", \"_\", \"%\", \"*\")");
						if (!answers[4])
							lblErrorPasses.setText("Пароли не совпадают.");
						if (!answers[5])
							lblErrorFName.setText("Некорректное имя. "
									+ "(Минимальная длина 2 символа. Только русские и латинские буквы)");
						if (!answers[6])
							lblErrorSName.setText("Некоректная фамилия. "
									+ "(Минимальная длина 2 символа. Только русские и латинские буквы)");
						if (!answers[7])
							lblErrorDate.setText("Некоректная дата рождения. "
									+ "(Формат даты дд.мм.гггг)");
						if (!answers[8])
							lblErrorNumber.setText("Некоректный телефон. "
									+ "(Минимальная длина 6 символов. Только цифры и знак + вначале)");
//						taInfo.setText(sb.toString());
					} else {
						logger.info("Creation error");
						lblErrorPair.setText("Ошибка при создании учетной записи");
					}
				} catch (IOException er) {
					logger.error("IO", er);
					er.printStackTrace();
				} catch (ClassNotFoundException er) {
					logger.error("ClassNotFound", er);
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
		});
		btnSignUp.setBounds(519, 384, 91, 23);
		contentPane.add(btnSignUp);

		JButton btnClear = new JButton("Очистить");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Clearing registration fields");
				/* Очищаем поля для регистрации */
				fldLogin.setText("");
				fldFirstPass.setText("");
				fldSecPass.setText("");
				fldFirstName.setText("");
				fldSecName.setText("");
				fldBirthDate.setText("");
				fldPhone.setText("");
			}
		});
		btnClear.setBounds(423, 384, 91, 23);
		contentPane.add(btnClear);
	}
	
	private void clearErrorLabels() {
		lblErrorPair.setText("");
		lblErrorLogin.setText("");
		lblErrorPass1.setText("");
		lblErrorPass2.setText("");
		lblErrorPasses.setText("");
		lblErrorFName.setText("");
		lblErrorSName.setText("");
		lblErrorDate.setText("");
		lblErrorNumber.setText("");
	}
}
