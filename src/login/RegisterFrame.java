package login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 * login
 * Created by ThaiBinh
 * Date 1/11/2022 - 11:42 PM
 * Description: ...
 */
public class RegisterFrame extends JFrame implements ActionListener {
    private Pattern checkName = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");
    private JButton btnConfirm, btnBack;
    private JTextField usernameField;
    private JTextField passwordField;
    private JPanel contentPane;

    public void RegisterFrame() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 640, 310);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel label = new JLabel("Register account");
        label.setForeground(Color.RED);
        label.setFont(new Font("Tahoma", Font.PLAIN, 32));
        label.setBounds(20, 0, 352, 49);
        contentPane.add(label);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(53, 51, 520, 140);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        usernameLabel.setBounds(26, 28, 136, 37);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        usernameField.setBounds(190, 30, 277, 37);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passwordLabel.setBounds(26, 84, 136, 13);
        panel.add(passwordLabel);

        passwordField = new JTextField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passwordField.setBounds(190, 72, 277, 37);
        panel.add(passwordField);

        btnConfirm = new JButton("Confirm");
        btnConfirm.setBounds(100, 210, 174, 38);
        btnConfirm.addActionListener(this);
        contentPane.add(btnConfirm);

        btnBack = new JButton("Back");
        btnBack.setBounds(320, 210, 174, 38);
        btnBack.addActionListener(this);
        contentPane.add(btnBack);

        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConfirm) {
            AccountManagement accountManagement = new AccountManagement();
            if (usernameField.getText().length() > 0 && passwordField.getText().length() > 0) {
                if (checkName.matcher(usernameField.getText()).matches() && passwordField.getText().contains(" ") == false) {
                    if (accountManagement.IsExisted(usernameField.getText()) == false) {
                        accountManagement.WriteFile(usernameField.getText(), passwordField.getText());
                        JOptionPane.showMessageDialog(this, "Register successfully!", "Register Notification", JOptionPane.ERROR_MESSAGE);
                        this.dispose();
                        LoginFrame frame = new LoginFrame();
                        frame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Username already existed! Please choose a different username", "Register Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Username or password contain invalid character", "Register Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in both fields!", "Register Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(e.getSource() == btnBack) {
            this.dispose();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        }
    }
}
