package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import tags.Tags;

public class MainFrame extends JFrame implements WindowListener {

	private JPanel contentPane;
	private Client clientNode;
	private static String IPClient = "", nameUser = "", dataUser = "";
	private static int portClient = 0;
	private static JList<String> onlineList;
	private static int portServer;
	private String name;
	static DefaultListModel<String> model = new DefaultListModel<>();
	String file = System.getProperty("user.dir") + "\\Server.txt";
	private JButton btnSaveServer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame(String arg, int arg1, String name, String msg, int port_Server) throws Exception {
		IPClient = arg;
		portClient = arg1;
		nameUser = name;
		dataUser = msg;
		portServer = port_Server;
		System.out.println("Port Server Main UI: " + portServer);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void updateFriendMainFrame(String msg) {
		model.addElement(msg);
	}

	public static void resetList() {
		model.clear();
	}

	void SaveServer() {
		try {
//			PrintWriter printWriter = new PrintWriter(new File(file));
//			StringBuilder stringBuilder = new StringBuilder();
//			stringBuilder.append(IPClient + " " + portServer);
//			printWriter.append(stringBuilder.toString());
//			printWriter.close();
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(IPClient + " " + portServer);
			bw.newLine();
			bw.close();

			JOptionPane.showMessageDialog(this, "Server information has been saved");
			btnSaveServer.setVisible(false);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * Create the frame.
	 *
	 * @throws Exception
	 */
	public MainFrame() throws Exception {
		this.addWindowListener(this);
		setResizable(false);

		System.out.println("Port Server Main UI: " + portServer);
		updateFriendMainFrame("none");
		clientNode = new Client(IPClient, portClient, nameUser, dataUser, portServer);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 677, 571);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Chat box");
		lblNewLabel.setForeground(Color.blue);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 32));
		lblNewLabel.setBounds(20, 10, 255, 64);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Welcome " + nameUser);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(20, 65, 309, 47);
		contentPane.add(lblNewLabel_1);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")), "Online list", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panel.setBackground(Color.WHITE);
		panel.setBounds(27, 164, 613, 344);

		contentPane.add(panel);
		panel.setLayout(new GridLayout(1, 1));

		onlineList = new JList<>(model);
		onlineList.setBorder(new EmptyBorder(5, 5, 5, 5));
		onlineList.setBackground(Color.WHITE);
		onlineList.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		onlineList.setBounds(10, 20, 577, 332);
		JScrollPane listPane = new JScrollPane(onlineList);
		panel.add(listPane);

		JPanel panel1 = new JPanel();
		panel1.setForeground(Color.BLUE);
		panel1.setBounds(453, 10, 187, 108);
		contentPane.add(panel1);
		panel1.setLayout(null);

		JLabel IPLabel = new JLabel("IP Address");
		IPLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		IPLabel.setBounds(10, 10, 85, 24);
		panel1.add(IPLabel);

		JLabel IPValue = new JLabel("127.0.0.1");
		try {
			IPValue.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		IPValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		IPValue.setBounds(88, 10, 115, 24);
		panel1.add(IPValue);

		JLabel portServerLabel = new JLabel("Port Server");
		portServerLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		portServerLabel.setBounds(10, 44, 85, 13);
		panel1.add(portServerLabel);

		JLabel portServerValue = new JLabel(String.valueOf(portServer));
		portServerValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		portServerValue.setBounds(88, 44, 74, 13);
		panel1.add(portServerValue);

		JLabel portClientLabel = new JLabel("Port Client");
		portClientLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		portClientLabel.setBounds(10, 73, 74, 13);
		panel1.add(portClientLabel);

		JLabel portClientValue = new JLabel(String.valueOf(portClient));
		portClientValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		portClientValue.setBounds(88, 73, 89, 13);
		panel1.add(portClientValue);

		btnSaveServer = new JButton("Save server information");
		btnSaveServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SaveServer();
			}
		});
		btnSaveServer.setFocusable(false);
		btnSaveServer.setBounds(460, 120, 171, 27);
		contentPane.add(btnSaveServer);
		onlineList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				name = onlineList.getModel().getElementAt(onlineList.locationToIndex(arg0.getPoint()));
				connectChat();
			}
		});
	}

	private void connectChat() {
		// TODO Auto-generated method stub
		int n = JOptionPane.showConfirmDialog(this, "Do you connect with this user?", "Connect",
				JOptionPane.YES_NO_OPTION);
		if (n == 0) {
			System.out.println(name);
			if (name.equals("") || Client.clientarray == null) {
				Tags.show(this, "Invalid username", false);
				return;
			}
			if (name.equals(nameUser)) {
				Tags.show(this, "This software doesn't support chat yourself function", false);
				return;
			}
			int size = Client.clientarray.size();
			for (int i = 0; i < size; i++) {
				if (name.equals(Client.clientarray.get(i).getName())) {
					try {
						clientNode.intialNewChat(Client.clientarray.get(i).getHost(),
								Client.clientarray.get(i).getPort(), name);
						return;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			Tags.show(this, "Friend is not found. Please wait to update your friend list", false);
		}
	}

	public static int request(String msg, boolean type) {
		JFrame frameMessage = new JFrame();
		return Tags.show(frameMessage, msg, type);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

		try {
			clientNode.exit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}
