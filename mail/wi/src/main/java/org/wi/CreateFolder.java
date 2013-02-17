package org.wi;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateFolder extends JFrame {

	private JPanel contentPane;
	
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private int key;
	private JTextField fldFoldName;

	public CreateFolder(final Socket socket, final DataInputStream in,
			final DataOutputStream out, final int key) {
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
		
		JButton btnCreateFold = new JButton("Создать папку");
		btnCreateFold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnCreateFold.setBounds(127, 74, 120, 23);
		contentPane.add(btnCreateFold);
		
		JLabel lblError = new JLabel("");
		lblError.setBounds(10, 49, 237, 14);
		contentPane.add(lblError);
		
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.key = key;
		
	}
}
