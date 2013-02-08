package org.wi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;

public class MainWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
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
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 731, 590);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea ta1 = new JTextArea();
		ta1.setLineWrap(true);
		JScrollPane spFolders = new JScrollPane(ta1);
		spFolders.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spFolders.setBounds(10, 28, 224, 177);
		contentPane.add(spFolders);
		
		JTextArea ta2 = new JTextArea();
		ta2.setLineWrap(true);
		JScrollPane spMessages = new JScrollPane(ta2);
		spMessages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spMessages.setBounds(265, 28, 447, 177);
		contentPane.add(spMessages);
		
		JTextArea ta3 = new JTextArea();
		ta3.setLineWrap(true);
		JScrollPane spMessageDetailed = new JScrollPane(ta3);
		spMessageDetailed.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spMessageDetailed.setBounds(10, 258, 702, 296);
		contentPane.add(spMessageDetailed);
		
		JLabel label = new JLabel("Папки");
		label.setBounds(10, 11, 46, 14);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Письма");
		label_1.setBounds(265, 11, 46, 14);
		contentPane.add(label_1);
		
		JButton button = new JButton("Удалить");
		button.setBounds(143, 216, 91, 23);
		contentPane.add(button);
		
		JButton button_1 = new JButton("Создать");
		button_1.setBounds(42, 216, 91, 23);
		contentPane.add(button_1);
		
		JButton button_2 = new JButton("Удалить");
		button_2.setBounds(621, 216, 91, 23);
		contentPane.add(button_2);
		
		JButton button_3 = new JButton("Написать");
		button_3.setBounds(520, 216, 91, 23);
		contentPane.add(button_3);
		
		JButton button_4 = new JButton("Переместить");
		button_4.setBounds(419, 216, 91, 23);
		contentPane.add(button_4);
	}
}
