package org.wi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;

import org.cc.SimpleMessage;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class NewMessage extends JFrame {

	private JPanel contentPane;
	private JTextField fldAbout;
	private JTextField fldTo;
	
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private int key;

	public NewMessage(final Socket socket, final DataInputStream in, 
			final DataOutputStream out, final int key) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 358);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.key = key;
		
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
				try {
					String[] requestInfo = { fldTo.getText().toLowerCase().trim(),
							fldAbout.getText().trim(), taMessage.getText().trim() };
					byte[] requestBytes = Serializer.serialize(requestInfo);
					out.writeByte(4);
					out.writeInt(key);
					out.writeInt(requestBytes.length);
					out.write(requestBytes);
					
					byte answer = in.readByte();
					if (answer == 0) {
						int length = in.readInt();
						byte[] answerBytes = new byte[length];
						for (int i = 0; i < length; i++)
							answerBytes[i] = in.readByte();
						SimpleMessage message = (SimpleMessage) Serializer
								.deserialize(answerBytes);
						
						lblErrorAbout.setText("");
						lblErrorTo.setText("");
						lblErrorMess.setText("");
						
					} else if (answer == 1) {
						int length = in.readInt();
						byte[] answerBytes = new byte[length];
						for (int i = 0; i < length; i++)
							answerBytes[i] = in.readByte();
						boolean[] data = (boolean[]) Serializer
								.deserialize(answerBytes);
						
						lblErrorAbout.setText("");
						lblErrorTo.setText("");
						lblErrorMess.setText("");
						
						if (!data[1])
							lblErrorTo.setText("Некорректный адресс получателя");
						if (!data[2])
							lblErrorAbout.setText("Некорректное поле \"Тема\"");
						if (!data[3])
							lblErrorMess.setText("Пустое сообщение");
						
					} else if (answer == 2) {
						lblErrorAbout.setText("");
						lblErrorTo.setText("");
						lblErrorMess.setText("");
						
						lblErrorMess.setText("Проблемы с сервером");
					} else {
						lblErrorAbout.setText("");
						lblErrorTo.setText("");
						lblErrorMess.setText("");
						
						lblErrorMess.setText("Несанкционированный пользователь");
					}
				} catch (IOException ex) {
					System.out.println("IO error");
					ex.printStackTrace();
					try {
						in.close();
						out.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (ClassNotFoundException er) {
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
		fldTo.setColumns(10);
		
		JLabel lblMailmailjs = new JLabel("Меня");
		lblMailmailjs.setBounds(83, 11, 81, 14);
		contentPane.add(lblMailmailjs);
	}
}
