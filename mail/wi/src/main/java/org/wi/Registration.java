package org.wi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Registration extends JFrame {

	private JPanel contentPane;
	private JTextField fldEnterMail;
	private JTextField fldEnterPass;
	private JTextField fldLogin;
	private JTextField fldFirstPass;
	private JTextField fldSecPass;
	private JTextField fldFirstName;
	private JTextField fldSecName;
	private JTextField fldBirthDate;
	private JTextField fldPhone;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registration frame = new Registration();
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
	public Registration() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 343);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblGfhjkm = new JLabel("Пароль:");
		lblGfhjkm.setBounds(163, 14, 53, 14);
		lblGfhjkm.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblGfhjkm);
		
		fldEnterMail = new JTextField();
		fldEnterMail.setBounds(53, 11, 100, 20);
		contentPane.add(fldEnterMail);
		fldEnterMail.setColumns(10);
		
		JLabel lblEmail = new JLabel("e-mail:");
		lblEmail.setBounds(10, 14, 46, 14);
		contentPane.add(lblEmail);
		
		fldEnterPass = new JTextField();
		fldEnterPass.setBounds(214, 11, 106, 20);
		contentPane.add(fldEnterPass);
		fldEnterPass.setColumns(10);
		
		JButton btnLogIn = new JButton("Войти");
		btnLogIn.setBounds(341, 10, 91, 23);
		contentPane.add(btnLogIn);
		
		JLabel label_1 = new JLabel("Регистрация");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		label_1.setBounds(10, 53, 132, 28);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("Логин:");
		label_2.setBounds(10, 92, 46, 14);
		contentPane.add(label_2);
		
		fldLogin = new JTextField();
		fldLogin.setBounds(77, 89, 100, 20);
		contentPane.add(fldLogin);
		fldLogin.setColumns(10);
		
		JLabel lblmailjs = new JLabel("@mail.js");
		lblmailjs.setBounds(177, 92, 46, 14);
		contentPane.add(lblmailjs);
		
		JLabel label_3 = new JLabel("Пароль:");
		label_3.setBounds(245, 92, 100, 14);
		contentPane.add(label_3);
		
		fldFirstPass = new JTextField();
		fldFirstPass.setBounds(346, 89, 86, 20);
		contentPane.add(fldFirstPass);
		fldFirstPass.setColumns(10);
		
		fldSecPass = new JTextField();
		fldSecPass.setBounds(346, 120, 86, 20);
		contentPane.add(fldSecPass);
		fldSecPass.setColumns(10);
		
		JLabel label_4 = new JLabel("Повторите пароль:");
		label_4.setBounds(245, 123, 106, 14);
		contentPane.add(label_4);
		
		fldFirstName = new JTextField();
		fldFirstName.setBounds(77, 152, 100, 20);
		contentPane.add(fldFirstName);
		fldFirstName.setColumns(10);
		
		fldSecName = new JTextField();
		fldSecName.setBounds(77, 183, 100, 20);
		contentPane.add(fldSecName);
		fldSecName.setColumns(10);
		
		fldBirthDate = new JTextField();
		fldBirthDate.setBounds(77, 214, 100, 20);
		contentPane.add(fldBirthDate);
		fldBirthDate.setColumns(10);
		
		fldPhone = new JTextField();
		fldPhone.setBounds(77, 242, 100, 20);
		contentPane.add(fldPhone);
		fldPhone.setColumns(10);
		
		JLabel label_5 = new JLabel("Имя:");
		label_5.setBounds(10, 155, 46, 14);
		contentPane.add(label_5);
		
		JLabel label_6 = new JLabel("Фамилия:");
		label_6.setBounds(10, 186, 67, 14);
		contentPane.add(label_6);
		
		JLabel label_7 = new JLabel("Дата рождения:");
		label_7.setBounds(10, 217, 67, 14);
		contentPane.add(label_7);
		
		JLabel label_8 = new JLabel("Телефон:");
		label_8.setBounds(10, 245, 67, 14);
		contentPane.add(label_8);
		
		JButton btnSignUp = new JButton("Зарег-я");
		btnSignUp.setBounds(341, 281, 91, 23);
		contentPane.add(btnSignUp);
		
		JButton btnClear = new JButton("Очистить");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnClear.setBounds(245, 281, 91, 23);
		contentPane.add(btnClear);
	}
}
