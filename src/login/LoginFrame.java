package login;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.MainFrame;
import tags.Encode;
import tags.Tags;

public class LoginFrame extends JFrame implements ActionListener {
	private Pattern checkName = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");
	private JPanel contentPane;
	private JTextField IPField;
	private JTextField portField;
	private JTextField usernameField;
	private JTextField passwordField;
	int port;
	String IP, username, password;
	JButton btnRegister, btnConnectServer;
	String file = System.getProperty("user.dir") + "\\Server.txt";
	List<String> listServer = new ArrayList<>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	void updateServer(String IP, String port) {
		IPField.setText(IP);
		portField.setText(port);
	}

	String[] readFileServer() throws FileNotFoundException {
		System.out.println(file);
		Scanner scanner = new Scanner(new File(file));
		while (scanner.hasNext()) {
			String server = scanner.nextLine();
			System.out.println(server + "-" + port);
			listServer.add(server);
		}
		scanner.close();
		String[] array = listServer.toArray(new String[0]);
		return array;
	}

	public LoginFrame() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("Login to Server");
		label.setForeground(Color.RED);
		label.setFont(new Font("Tahoma", Font.PLAIN, 32));
		label.setBounds(20, 0, 352, 49);
		contentPane.add(label);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(53, 51, 520, 260);
		contentPane.add(panel);
		panel.setLayout(null);

		JComboBox comboBox = new JComboBox();
		comboBox.setForeground(Color.BLUE);
		String[] data = null;
		try {
			data = readFileServer();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (data == null) {
			comboBox.setVisible(false);
		} else {
			comboBox.setModel(new DefaultComboBoxModel(data));
			comboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox) e.getSource();
					String ss = (String) cb.getSelectedItem();
					String[] s = ss.split(" ");
					updateServer(s[0], s[1]);
				}
			});
		}
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBox.setBounds(190, 5, 144, 37);
		panel.add(comboBox);

		JLabel IPLabel = new JLabel("IP Address Server");
		IPLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		IPLabel.setBounds(26, 58, 136, 37);
		panel.add(IPLabel);

		IPField = new JTextField();
		IPField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		IPField.setBounds(190, 60, 277, 37);
		panel.add(IPField);
		IPField.setColumns(10);
		try {
			IPField.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		JLabel portLabel = new JLabel("Port");
		portLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		portLabel.setBounds(26, 114, 45, 13);
		panel.add(portLabel);

		portField = new JTextField();
		portField.setText("8080");
		portField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		portField.setBounds(190, 102, 277, 37);
		panel.add(portField);
		portField.setColumns(10);

		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameLabel.setBounds(26, 145, 84, 37);
		panel.add(usernameLabel);

		usernameField = new JTextField();
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameField.setBounds(190, 145, 277, 37);
		panel.add(usernameField);
		usernameField.setColumns(10);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordLabel.setBounds(26, 190, 84, 37);
		panel.add(passwordLabel);

		passwordField = new JTextField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordField.setBounds(190, 188, 277, 37);
		panel.add(passwordField);
		passwordField.setColumns(10);

		btnRegister = new JButton("Register");
		btnRegister.setBounds(320, 320, 174, 38);
		btnRegister.addActionListener(this);
		contentPane.add(btnRegister);

		btnConnectServer = new JButton("Connect");
		btnConnectServer.setBounds(100, 320, 174, 38);
		btnConnectServer.addActionListener(this);
		contentPane.add(btnConnectServer);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnRegister) {
			this.dispose();
			RegisterFrame frame = new RegisterFrame();
			frame.RegisterFrame();
		}
		if (e.getSource() == btnConnectServer) {
			username = usernameField.getText();
			password = passwordField.getText();
			IP = IPField.getText();
			AccountManagement accountManagement = new AccountManagement();

			if (checkName.matcher(username).matches() == false) {
				JOptionPane.showMessageDialog(this, "Username field is empty or contain invalid character", "Login Error", JOptionPane.ERROR_MESSAGE);
			} else if (password.length() == 0) {
				JOptionPane.showMessageDialog(this, "Password field is empty!", "Login Error", JOptionPane.ERROR_MESSAGE);
			} else if (accountManagement.IsExisted(username) == false) {
				JOptionPane.showMessageDialog(this, "Username not existed!", "Login Error", JOptionPane.ERROR_MESSAGE);
			} else if (accountManagement.CheckLogin(username, password) == false) {
				JOptionPane.showMessageDialog(this, "Wrong password!", "Login Error", JOptionPane.ERROR_MESSAGE);
			}
			if (checkName.matcher(username).matches() && !IP.equals("") && accountManagement.CheckLogin(username, password) == true) {
				try {
					Random rd = new Random();
					int portPeer = 10000 + rd.nextInt() % 1000;
					InetAddress ipServer = InetAddress.getByName(IP);
					int portServer = Integer.parseInt(portField.getText());
					Socket socketClient = new Socket(ipServer, portServer);

					String msg = Encode.getCreateAccount(username, Integer.toString(portPeer));
					ObjectOutputStream serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
					serverOutputStream.writeObject(msg);
					serverOutputStream.flush();
					ObjectInputStream serverInputStream = new ObjectInputStream(socketClient.getInputStream());
					msg = (String) serverInputStream.readObject();

					socketClient.close();
					if (msg.equals(Tags.SESSION_DENY_TAG)) {
						JOptionPane.showMessageDialog(this, "This account is already in used!", "Login Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					System.out.println("Port Server Login: " + portServer);
					new MainFrame(IP, portPeer, username, msg, portServer);
					this.dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Turn on server before connecting", "Login Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		}
	}
}
