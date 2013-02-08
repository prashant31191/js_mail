package org.wi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.DropMode;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;

public class NewMessage extends JFrame {

	private JPanel contentPane;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewMessage frame = new NewMessage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NewMessage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JTextArea taMessage = new JTextArea();
		taMessage.setLineWrap(true);
		JScrollPane sp = new JScrollPane(taMessage);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setBounds(10, 153, 422, 133);
		contentPane.add(sp);
		
		JButton btnSend = new JButton("Отправить");
		btnSend.setBounds(341, 297, 91, 23);
		contentPane.add(btnSend);
		
		JButton btnClear = new JButton("Очистить");
		btnClear.setBounds(243, 297, 91, 23);
		contentPane.add(btnClear);
		
		textField_1 = new JTextField();
		textField_1.setBounds(83, 86, 349, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(83, 47, 349, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblMailmailjs = new JLabel("mail@mail.js");
		lblMailmailjs.setBounds(83, 11, 81, 14);
		contentPane.add(lblMailmailjs);
	}
}
