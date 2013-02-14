package org.wi;

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

import org.cc.SimpleFolder;
import org.cc.SimpleMessage;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWindow extends JFrame {

	private JPanel contentPane;

	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private int key;
	
	List<SimpleFolder> folders = null;
	List<SimpleMessage> messages = null;
	DefaultListModel<String> listModelMessages = new DefaultListModel<String>();
	DefaultListModel<String> listModelFolders = new DefaultListModel<String>();
	
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
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					out.writeByte(0);
				} catch (IOException e1) {
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
		
		final JTextArea taMessText = new JTextArea();
		taMessText.setEditable(false);
		JScrollPane spMessageDetailed = new JScrollPane(taMessText);
		spMessageDetailed.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spMessageDetailed.setBounds(10, 258, 702, 296);
		contentPane.add(spMessageDetailed);
		
		final JList<String> lstFolders = new JList<String>(listModelFolders);
		lstFolders.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selected = lstFolders.getSelectedIndex();
				if (selected > -1) {
					listModelMessages.removeAllElements();
					SimpleFolder folder = folders.get(selected);
					List<SimpleMessage> messages = folder.getMessages();
					for (SimpleMessage message: messages) {
						listModelMessages.add(messages.indexOf(message), message.getText());
					}
				}
			}
		});
		JScrollPane spFolders = new JScrollPane(lstFolders);
		spFolders.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spFolders.setBounds(10, 28, 224, 177);
		contentPane.add(spFolders);
		
		
		final JList<String> lstMessages = new JList<String>(listModelMessages);
		lstMessages.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedFolder = lstFolders.getSelectedIndex();
				if (selectedFolder > -1) {
					int selectedMessage = lstMessages.getSelectedIndex();
					if (selectedMessage > -1) {
						SimpleMessage message = folders.get(selectedFolder).getMessages().get(selectedMessage);
						StringBuilder sb = new StringBuilder();
						sb.append(message.getDate() + "\n");
						sb.append("От: " + message.getFrom() + "\n");
						sb.append("Кому: " + message.getTo() + "\n");
						sb.append("Тема: " + message.getAbout() + "\n\n");
						sb.append(message.getText());
						taMessText.setText(sb.toString());
					}
				}
			}
		});
		JScrollPane spMessages = new JScrollPane(lstMessages);
		spMessages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spMessages.setBounds(265, 28, 447, 177);
		contentPane.add(spMessages);
		
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
		
		
		try {
			out.writeByte(3);
			out.writeInt(key);
			
			byte answer = in.readByte();
			if (answer == 1) {
				taMessText.setText("Несанкционированный пользователь");
			} else if (answer == 2) {
				taMessText.setText("Проблемы с сервером");
			} else {
				int length = in.readInt();
				byte[] answerBytes = new byte[length];
				for (int i = 0; i < length; i++)
					answerBytes[i] = in.readByte();
				folders = (List<SimpleFolder>) Serializer
						.deserialize(answerBytes);
			}
		} catch (IOException er) {
			er.printStackTrace();
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
		
		for (SimpleFolder sf : folders) {
			listModelFolders.add(folders.indexOf(sf), sf.getName());
		}
		
	}
}
