package login;

import java.io.*;
import java.util.TreeMap;

/**
 * login
 * Created by ThaiBinh
 * Date 1/12/2022 - 10:43 PM
 * Description: ...
 */
public class AccountManagement {
    private TreeMap<String, String> accMap;

    public AccountManagement() {
        accMap = new TreeMap<>();
    }

    public AccountManagement(TreeMap<String, String> accMap) {
        this.accMap = accMap;
    }

    public void WriteFile(String username, String password) {
        FileWriter fw;
        try {
            File file = new File("Account.txt");
            fw = new FileWriter(file, true);
            fw.write(username + " " + password + "\n");
            fw.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean IsExisted(String username) {
        FileReader fr;
        String s = "";
        try {
            fr = new FileReader("Account.txt");
            int data = fr.read();
            StringBuilder line = new StringBuilder();
            while (data != -1) {
                if ((char)data == '\n') {
                    String temp = s;
                    s = temp + line.toString();
                    String[] accountCheck = s.split(" ");
                    if(accountCheck[0].equals(username)) {
                        return true;
                    }
                    s = "";
                    line.delete(0, line.length());
                    data = fr.read();
                    continue;
                }
                line.append((char)data);
                data = fr.read();
            }
            fr.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean CheckLogin(String username, String password) {
        FileReader fr;
        String s = "";
        try {
            fr = new FileReader("Account.txt");
            int data = fr.read();
            StringBuilder line = new StringBuilder();
            while (data != -1) {
                if ((char)data == '\n') {
                    String temp = s;
                    s = temp + line.toString();
                    String[] accountCheck = s.split(" ");
                    if(accountCheck[0].equals(username) && accountCheck[1].equals(password)) {
                        return true;
                    }
                    s = "";
                    line.delete(0, line.length());
                    data = fr.read();
                    continue;
                }
                line.append((char)data);
                data = fr.read();
            }
            fr.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
