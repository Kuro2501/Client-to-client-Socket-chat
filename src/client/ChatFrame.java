package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import data.DataFile;
import tags.Decode;
import tags.Encode;
import tags.Tags;

public class ChatFrame extends JFrame {
	// Socket
	private static String URL_DIR = System.getProperty("user.dir");
	private Socket socketChat;
	private String nameUser = "", nameGuest = "", nameFile = "";
	public boolean isStop = false, isSendFile = false, isReceiveFile = false;
	private ChatRoom chat;
	private int portServer = 0;

	// Frame
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextPane txtDisplayMessage;
	private JButton btnSendFile;
	private JLabel lblReceive;
	private ChatFrame frame = this;
	private JProgressBar progressBar;
	JButton btnSend;

	public ChatFrame(String user, String guest, Socket socket, int port) throws Exception {
		super();
		nameUser = user;
		nameGuest = guest;
		socketChat = socket;
		frame = new ChatFrame(user, guest, socket, port, port);
		frame.setVisible(true);
	}

	public ChatFrame(String user, String guest, Socket socket, int port, int a) throws Exception {
		// TODO Auto-generated constructor stub
		super();
		nameUser = user;
		nameGuest = guest;
		socketChat = socket;
		this.portServer = port;
		System.out.println("user: " + user);
		System.out.println("Guest: " + guest);
		System.out.println("Port: " + port);
		System.out.println("Socket: " + socket);
		chat = new ChatRoom(socketChat, nameUser, nameGuest);
		chat.start();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					initial();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void updateChat_receive(String msg) {
		appendToPane(txtDisplayMessage, "<div class='left' style='width: 40%; background-color: #f1f0f0;'>" + "    "
				+ msg + "<br>" + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "</div>");
	}

	public void updateChat_send(String msg) {
		appendToPane(txtDisplayMessage,
				"<table class='bang' style='color: white; clear:both; width: 100%;'>" + "<tr align='right'>"
						+ "<td style='width: 59%; '></td>" + "<td style='width: 40%; background-color: #0084ff;'>"
						+ LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "<br>" + msg
						+ "</td> </tr>" + "</table>");
	}

	public void updateChat_notify(String msg) {
		appendToPane(txtDisplayMessage,
				"<table class='bang' style='color: white; clear:both; width: 100%;'>" + "<tr align='right'>"
						+ "<td style='width: 59%; '></td>" + "<td style='width: 40%; background-color: #f1c40f;'>" + msg
						+ "</td> </tr>" + "</table>");
	}

	/**
	 * Create the frame.
	 */
	public void initial() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev) {
				try {
					isStop = true;
					frame.dispose();
					chat.sendMessage(Tags.CHAT_CLOSE_TAG);
					chat.stopChat();
					System.gc();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		setResizable(false);
		setTitle("Chat Frame");
		setBounds(100, 100, 660, 510);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 660, 40);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel nameLabel = new JLabel(nameGuest);
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		nameLabel.setToolTipText("");
		nameLabel.setBounds(5, 0, 150, 38);
		panel.add(nameLabel);

		JPanel panel1 = new JPanel();
		txtDisplayMessage = new JTextPane();
		txtDisplayMessage.setEditable(false);
		txtDisplayMessage.setContentType("text/html");
		txtDisplayMessage.setBackground(Color.GRAY);
		txtDisplayMessage.setForeground(Color.WHITE);
		txtDisplayMessage.setFont(new Font("Courier New", Font.PLAIN, 18));
		appendToPane(txtDisplayMessage, "<div class='clear' style='background-color:white'></div>"); // set default

		panel1.setBounds(5, 40, 650, 323);
		panel1.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(txtDisplayMessage);
		scrollPane.setBounds(0, 0, 650, 323);
		panel1.add(scrollPane);
		contentPane.add(panel1);

		JPanel panel2 = new JPanel();
		panel2.setBounds(5, 365, 650, 10);
		contentPane.add(panel2);
		panel2.setLayout(null);

		progressBar = new JProgressBar();
		progressBar.setBounds(0, 0, 650, 10);
		progressBar.setVisible(false);
		panel2.add(progressBar);

		JPanel panel3 = new JPanel();
		panel3.setBounds(0, 380, 660, 85);
		panel3.setLayout(null);

		btnSend = new JButton();
		btnSend.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btnSend.setContentAreaFilled(false);
		btnSend.setText("Send");
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = txtMessage.getText();
				if (msg.equals(""))
					return;
				txtMessage.setText("");
				try {
					chat.sendMessage(Encode.sendMessage(msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send(msg);

			}
		});
		btnSend.setBounds(550, 0, 70, 34);
		panel3.add(btnSend);

		btnSendFile = new JButton();
		btnSendFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					isSendFile = true;
					String path_send = (fileChooser.getSelectedFile().getAbsolutePath());
					System.out.println(path_send);
					nameFile = fileChooser.getSelectedFile().getName();
					File file = fileChooser.getSelectedFile();
					// if (isSendFile)
					try {
						chat.sendMessage(Encode.sendFile(nameFile));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					System.out.println("nameFile: " + nameFile);
					try {
						chat.sendFile(file);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnSendFile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btnSendFile.setContentAreaFilled(false);
		btnSendFile.setText("Send file");
		btnSendFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSendFile.setBounds(550, 40, 70, 34);
		panel3.add(btnSendFile);

		txtMessage = new JTextField();
		txtMessage.setBounds(5, 10, 500, 50);
		panel3.add(txtMessage);
		txtMessage.setColumns(10);
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnSend.doClick();
				}
			}
		});
		contentPane.add(panel3);

		lblReceive = new JLabel("");
		lblReceive.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblReceive.setBounds(47, 520, 450, 29);
		lblReceive.setVisible(false);
		contentPane.add(lblReceive);
	}

	public class ChatRoom extends Thread {
		private Socket connect;
		private ObjectOutputStream outPeer;
		private ObjectInputStream inPeer;
		private boolean continueSendFile = true, finishReceive = false;
		private int sizeOfSend = 0, sizeOfData = 0, sizeFile = 0, sizeReceive = 0;
		private String nameFileReceive = "";
		private InputStream inFileSend;
		private DataFile dataFile;

		public ChatRoom(Socket connection, String name, String guest) throws Exception {
			connect = new Socket();
			connect = connection;
			nameGuest = guest;
			System.out.println(connect);
		}

		@Override
		public void run() {
			super.run();
			System.out.println("Chat Room start");
			OutputStream out = null;
			while (!isStop) {
				try {
					inPeer = new ObjectInputStream(connect.getInputStream());
					Object obj = inPeer.readObject();
					if (obj instanceof String) {
						String msgObj = obj.toString();
						if (msgObj.equals(Tags.CHAT_CLOSE_TAG)) {
							isStop = true;
							Tags.show(frame, nameGuest + " closed chat with you! This windows will also be closed.",
									false);
							try {
								isStop = true;
								frame.dispose();
								chat.sendMessage(Tags.CHAT_CLOSE_TAG);
								chat.stopChat();
								System.gc();
							} catch (Exception e) {
								e.printStackTrace();
							}
							connect.close();
							break;
						}
						if (Decode.checkFile(msgObj)) {
							System.out.println("Check file: " + URL_DIR + "/" + nameFileReceive);
							isReceiveFile = true;
							nameFileReceive = msgObj.substring(10, msgObj.length() - 11);
							File fileReceive = new File(URL_DIR + "/" + nameFileReceive);
							if (!fileReceive.exists()) {
								fileReceive.createNewFile();
							}
							String msg = Tags.FILE_REQ_ACK_OPEN_TAG + Integer.toBinaryString(portServer)
									+ Tags.FILE_REQ_ACK_CLOSE_TAG;
							sendMessage(msg);

						} else if (Decode.checkFeedBack(msgObj)) {
							btnSendFile.setEnabled(false);

							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										sendMessage(Tags.FILE_DATA_BEGIN_TAG);
										updateChat_notify("You are sending file: " + nameFile);
										isSendFile = false;
//										sendFile(txtMessage.getText());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).start();
						} else if (msgObj.equals(Tags.FILE_DATA_BEGIN_TAG)) {
							finishReceive = false;
							lblReceive.setVisible(true);
							out = new FileOutputStream(URL_DIR + nameFileReceive);
						} else if (msgObj.equals(Tags.FILE_DATA_CLOSE_TAG)) {
							System.out.println("Close file: " + URL_DIR + "\\" + nameFileReceive);

							updateChat_receive(
									"You receive file: " + nameFileReceive + " with size " + sizeReceive + " KB");
							sizeReceive = 0;
							out.flush();
							out.close();
							lblReceive.setVisible(false);
							System.out.println("Chon vi tri luu file");

							new Thread(new Runnable() {

								@Override
								public void run() {
									System.out.println("Chon vi tri luu file");
									showSaveFile();
								}
							}).start();
							finishReceive = true;
//						} else if (msgObj.equals(Tags.FILE_DATA_CLOSE_TAG) && isFileLarge == true) {
//							updateChat_receive("File " + nameFileReceive + " too large to receive");
//							sizeReceive = 0;
//							out.flush();
//							out.close();
//							lblReceive.setVisible(false);
//							finishReceive = true;
						} else {

							String message = Decode.getMessage(msgObj);

							updateChat_receive(message);
						}
					} else if (obj instanceof DataFile) {

						DataFile data = (DataFile) obj;
						++sizeReceive;
						out.write(data.data);
					}
				} catch (Exception e) {
					File fileTemp = new File(URL_DIR + nameFileReceive);
					if (fileTemp.exists() && !finishReceive) {
						fileTemp.delete();
					}
				}
			}
		}

		private void getData(File file) throws Exception {
			File fileData = file;
			if (fileData.exists()) {
				sizeOfSend = 0;
				dataFile = new DataFile();
				sizeFile = (int) fileData.length();
				sizeOfData = sizeFile % 1024 == 0 ? (int) (fileData.length() / 1024)
						: (int) (fileData.length() / 1024) + 1;
				inFileSend = new FileInputStream(fileData);
			}
		}

		public void sendFile(File file) throws Exception {

			btnSendFile.setEnabled(false);
			getData(file);
			lblReceive.setVisible(true);
			if (sizeOfData > Tags.MAX_MSG_SIZE / 1024) {
				lblReceive.setText("File is too large...");
				inFileSend.close();
				sendMessage(Tags.FILE_DATA_CLOSE_TAG);
				btnSendFile.setEnabled(true);
				isSendFile = false;
				inFileSend.close();
				return;
			}

			progressBar.setVisible(true);
			progressBar.setValue(0);

			lblReceive.setText("Sending ...");
			do {
				System.out.println("sizeOfSend : " + sizeOfSend);
				if (continueSendFile) {
					continueSendFile = false;
//					updateChat_notify("If duoc thuc thi: " + String.valueOf(continueSendFile));
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								inFileSend.read(dataFile.data);
								sendMessage(dataFile);
								sizeOfSend++;
								if (sizeOfSend == sizeOfData - 1) {
									int size = sizeFile - sizeOfSend * 1024;
									dataFile = new DataFile(size);
								}
								progressBar.setValue(sizeOfSend * 100 / sizeOfData);
								if (sizeOfSend >= sizeOfData) {
									inFileSend.close();
									isSendFile = true;
									sendMessage(Tags.FILE_DATA_CLOSE_TAG);
									progressBar.setVisible(false);
									lblReceive.setVisible(false);
									isSendFile = false;
									btnSendFile.setEnabled(true);
									updateChat_notify("File sent complete");
									inFileSend.close();
								}
								continueSendFile = true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			} while (sizeOfSend < sizeOfData);
		}

		private void showSaveFile() {
			System.out.println("Chon vi tri luu file");
			while (true) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showSaveDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = new File(fileChooser.getSelectedFile().getAbsolutePath() + "/" + nameFileReceive);
					if (!file.exists()) {
						try {
							file.createNewFile();
							Thread.sleep(1000);
							InputStream input = new FileInputStream(URL_DIR + nameFileReceive);
							OutputStream output = new FileOutputStream(file.getAbsolutePath());
							copyFileReceive(input, output, URL_DIR + nameFileReceive);
						} catch (Exception e) {
							Tags.show(frame, "Your file receive has error!!!", false);
						}
						break;
					} else {
						int resultContinue = Tags.show(frame, "File is exists. You want save file?", true);
						if (resultContinue == 0)
							continue;
						else
							break;
					}
				}
			}
		}

		// void send Message
		public synchronized void sendMessage(Object obj) throws Exception {
			outPeer = new ObjectOutputStream(connect.getOutputStream());
			// only send text
			if (obj instanceof String) {
				String message = obj.toString();
				outPeer.writeObject(message);
				outPeer.flush();
				if (isReceiveFile)
					isReceiveFile = false;
			}
			// send attach file
			else if (obj instanceof DataFile) {
				outPeer.writeObject(obj);
				outPeer.flush();
			}
		}

		public void stopChat() {
			try {
				connect.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void copyFileReceive(InputStream inputStr, OutputStream outputStr, String path) throws IOException {
		byte[] buffer = new byte[1024];
		int lenght;
		while ((lenght = inputStr.read(buffer)) > 0) {
			outputStr.write(buffer, 0, lenght);
		}
		inputStr.close();
		outputStr.close();
		File fileTemp = new File(path);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}
	}

	private void appendToPane(JTextPane tp, String msg) {
		HTMLDocument doc = (HTMLDocument) tp.getDocument();
		HTMLEditorKit editorKit = (HTMLEditorKit) tp.getEditorKit();
		try {

			editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
			tp.setCaretPosition(doc.getLength());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
