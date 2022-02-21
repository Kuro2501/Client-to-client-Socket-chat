package server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import data.Peer;

public class ServerFrame extends JFrame {

	private JPanel contentPane;
	private JTextField IPField;
	private JTextField portField;
	private JLabel status;
	private static JTextArea statusBox;
	public static JLabel numbOfUsers;
	public static int port = 8080;
	static ServerCore server;
	JButton btnStopServer, btnStartServer;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void updateMessage(String msg) {
		statusBox.append(msg + "\n");
	}

	public static void updateNumberClient() {
		int number = Integer.parseInt(numbOfUsers.getText());
		numbOfUsers.setText(Integer.toString(number + 1));
		displayUser();

	}

	public static void decreaseNumberClient() {
		int number = Integer.parseInt(numbOfUsers.getText());
		numbOfUsers.setText(Integer.toString(number - 1));
		displayUser();

	}

	static void displayUser() {
		statusBox.setText("");
		ArrayList<Peer> list = server.getListUser();
		for (int i = 0; i < list.size(); i++) {
			statusBox.append((i + 1) + "\t" + list.get(i).getName() + "\n");
		}
	}

	public ServerFrame() {
		setResizable(false);
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 795, 710);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Server");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 32));
		lblNewLabel.setBounds(20, 0, 245, 76);
		lblNewLabel.setForeground(Color.RED);
		contentPane.add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBounds(31, 100, 279, 34);
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JLabel IPLabel = new JLabel("IP Address");
		IPLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel.add(IPLabel);

		panel.add(new JPanel());
		IPField = new JTextField();
		IPField.setEditable(false);
		IPField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel.add(IPField);
		IPField.setColumns(10);
		try {
			IPField.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		JPanel panel1 = new JPanel();
		panel1.setBounds(31, 154, 279, 34);
		contentPane.add(panel1);
		panel1.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("Port");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(0, 10, 55, 24);
		panel1.add(lblNewLabel_2);

		JPanel panel2 = new JPanel();
		panel2.setBounds(18, 0, 10, 34);
		panel1.add(panel2);

		portField = new JTextField();
		portField.setText("8080");
		portField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		portField.setBounds(93, 0, 185, 34);
		panel1.add(portField);
		portField.setColumns(10);

		JPanel panel3 = new JPanel();
		panel3.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Server Information", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel3.setBounds(432, 93, 330, 130);
		contentPane.add(panel3);
		panel3.setLayout(null);

		JLabel statusLabel = new JLabel("Status");
		statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		statusLabel.setBounds(22, 36, 74, 17);
		panel3.add(statusLabel);

		status = new JLabel("Off");
		status.setForeground(Color.RED);
		status.setFont(new Font("Tahoma", Font.PLAIN, 18));
		status.setBounds(223, 23, 124, 43);
		panel3.add(status);

		JLabel numbOfUsersLabel = new JLabel("Number of Users");
		numbOfUsersLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		numbOfUsersLabel.setBounds(22, 80, 184, 32);
		panel3.add(numbOfUsersLabel);

		numbOfUsers = new JLabel("0");
		numbOfUsers.setFont(new Font("Tahoma", Font.PLAIN, 18));
		numbOfUsers.setBounds(223, 80, 84, 32);
		panel3.add(numbOfUsers);

		JPanel startStop = new JPanel();
		startStop.setBounds(0, 200, 340, 64);
		contentPane.add(startStop);

		btnStartServer = new JButton("Start Server");
		btnStartServer.setFocusable(false);
		btnStartServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					port = Integer.valueOf(portField.getText());
					server = new ServerCore(port);
					ServerFrame.updateMessage("Start server on port " + port);
					status.setText("Running...");
					btnStopServer.setEnabled(true);
					btnStartServer.setEnabled(false);
				} catch (Exception e1) {
					ServerFrame.updateMessage("Start Error!!!");
					e1.printStackTrace();
				}
			}
		});
		btnStartServer.setFont(new Font("Tahoma", Font.PLAIN, 18));
		startStop.add(btnStartServer);

		JPanel panel5 = new JPanel();
		startStop.add(panel5);
		startStop.add(new JPanel());

		btnStopServer = new JButton("Stop Server");
		btnStopServer.setEnabled(false);
		btnStopServer.setFocusable(false);
		btnStopServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				numbOfUsers.setText("0");
				try {
					server.stopserver();
					ServerFrame.updateMessage("Stop server");
					status.setText("Off");
					btnStopServer.setEnabled(false);
					btnStartServer.setEnabled(true);
				} catch (Exception ex) {
					ex.printStackTrace();
					ServerFrame.updateMessage("Stop server");
					status.setText("Off");
					btnStopServer.setEnabled(false);
					btnStartServer.setEnabled(true);
				}
			}
		});
		btnStopServer.setFont(new Font("Tahoma", Font.PLAIN, 18));
		startStop.add(btnStopServer);

		JPanel panel6 = new JPanel();
		panel6.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		statusBox = new JTextArea();
		statusBox.setBackground(Color.BLACK);
		statusBox.setForeground(Color.WHITE);
		statusBox.setFont(new Font("Courier New", Font.PLAIN, 18));
		panel6.setBounds(20, 260, 740, 400);
		panel6.setLayout(new GridLayout(0, 1, 0, 0));
		JScrollPane scrollPane = new JScrollPane(statusBox);
		panel6.add(scrollPane);
		contentPane.add(panel6);
	}

}
