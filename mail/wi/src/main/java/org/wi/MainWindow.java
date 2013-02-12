package org.wi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingConstants;

public class MainWindow extends JFrame {

	private JPanel contentPane;

	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private int key;
	
	public MainWindow(final Socket socket, final DataInputStream in, 
			final DataOutputStream out, int key) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 731, 590);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.key = key;
		
		JList<String> lstFolders = new JList<String>();
		JScrollPane spFolders = new JScrollPane(lstFolders);
		spFolders.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spFolders.setBounds(10, 28, 224, 177);
		contentPane.add(spFolders);
		
		JList<String> lstMessages = new JList<String>();
		JScrollPane spMessages = new JScrollPane(lstMessages);
		spMessages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spMessages.setBounds(265, 28, 447, 177);
		contentPane.add(spMessages);
		
		final JTextArea taMessText = new JTextArea("" + key);
		taMessText.setEditable(false);
		JScrollPane spMessageDetailed = new JScrollPane(taMessText);
		spMessageDetailed.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spMessageDetailed.setBounds(10, 258, 702, 296);
		contentPane.add(spMessageDetailed);
		
		JLabel label = new JLabel("Папки");
		label.setBounds(10, 11, 46, 14);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Письма");
		label_1.setBounds(265, 11, 46, 14);
		contentPane.add(label_1);
		
		JButton btnDeleteFolder = new JButton("Удалить");
		btnDeleteFolder.setBounds(143, 216, 91, 23);
		contentPane.add(btnDeleteFolder);
		
		JButton btnCreateFolder = new JButton("Создать");
		btnCreateFolder.setBounds(42, 216, 91, 23);
		contentPane.add(btnCreateFolder);
		
		JButton btnDelMess = new JButton("Удалить");
		btnDelMess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String requestInfo = "PAPA";

					byte[] requestBytes = Serializer.serialize(requestInfo);
					out.writeByte(3);
					out.writeInt(requestBytes.length);
					out.write(requestBytes);

					boolean answer = in.readBoolean();
					if (answer) {
						int length = in.readInt();
						byte[] answerBytes = new byte[length];
						for (int i = 0; i < length; i++)
							answerBytes[i] = in.readByte();
						String answers = (String) Serializer
								.deserialize(answerBytes);
						taMessText.setText(answers);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					try {
						in.close();
						out.close();
						socket.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
					try {
						in.close();
						out.close();
						socket.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		btnDelMess.setBounds(621, 216, 91, 23);
		contentPane.add(btnDelMess);
		
		JButton btnCreatMess = new JButton("Написать");
		btnCreatMess.setBounds(520, 216, 91, 23);
		contentPane.add(btnCreatMess);
		
		JButton btnMoveMess = new JButton("Переместить");
		btnMoveMess.setBounds(419, 216, 91, 23);
		contentPane.add(btnMoveMess);
	}
}
