import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class loginPage extends JFrame implements ActionListener, ItemListener{

	private JPanel northPanel, centerPanel, southPanel;
	private JButton bLogin;
	private JCheckBox chkPass;
	private JLabel lUsername, lPass, welcome;
	private JTextField tfUsername;
	private JPasswordField pfPass;

	Connect con;


	public loginPage() {
		setSize(500,350);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("LOGIN PAGE");
		setLocationRelativeTo(null);
		init();
	}

	private void init() {
		con = new Connect();

		//NORTH 
		welcome = new JLabel("Welcome !");
		welcome.setForeground(Color.DARK_GRAY);

		northPanel = new JPanel();
		northPanel.add(welcome);


		//CENTER
		lUsername = new JLabel("Username : ");
		lPass = new JLabel("Password : ");
		tfUsername = new JTextField();
		pfPass = new JPasswordField();

		centerPanel = new JPanel(new GridLayout(2,2,20,30));
		centerPanel.setBorder(new EmptyBorder(50,35,50,35));
		centerPanel.add(lUsername);
		centerPanel.add(tfUsername);
		centerPanel.add(lPass);
		centerPanel.add(pfPass);

		//SOUTH
		bLogin = new JButton("LOGIN");
		bLogin.addActionListener(this);
		chkPass = new JCheckBox("Show Password");
		chkPass.addItemListener(this);

		southPanel = new JPanel(new GridLayout(2,1,20,30));
		southPanel.setBorder(new EmptyBorder(0,100,40,100));
		southPanel.add(chkPass);
		southPanel.add(bLogin);
		//ADD
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		pack();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		//ref: https://stackoverflow.com/questions/20812857/how-to-display-characters-in-jpasswordfield-rather-than-sign-in-java
		if (e.getStateChange() == ItemEvent.DESELECTED) {
			pfPass.setEchoChar('•');
		} else
			pfPass.setEchoChar((char) 0);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//CHECK USERNAME AND PASSWORD
		ResultSet rs;
		String tempUser = tfUsername.getText();
		String tempPass = String.valueOf(pfPass.getPassword());

		if (tempUser.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Username must be filled!", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (tempPass.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Password must be filled!", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			boolean uTrue = false;
			boolean pTrue = false;
			String uCheck = "SELECT username FROM staff";
			rs = con.executeQuery(uCheck);
			while (rs.next()) {
				String username = rs.getString("username");
				if (tempUser.equals(username)) {
					uTrue = true;
					break;
				}
			}
			if (uTrue) {
				String pCheck = "SELECT password FROM staff WHERE username='%s'";
				pCheck = String.format(pCheck, tempUser);
				rs = con.executeQuery(pCheck);
				rs.next();
				String password = rs.getString("password");
				if (tempPass.equals(password)) {
					pTrue = true;
				}
			}
			if (pTrue && uTrue) {
				JOptionPane.showMessageDialog(null, "Login Successful!", "Welcome!", JOptionPane.INFORMATION_MESSAGE);
				String idCheck = "SELECT id FROM staff WHERE username='%s'";
				idCheck = String.format(idCheck, tempUser);
				rs = con.executeQuery(idCheck);
				rs.next();
				int id = rs.getInt("id");
				new MainFrame(id).setVisible(true);
				dispose();
			} else
				JOptionPane.showMessageDialog(null, "Incorrect Username or Password!", "ERROR", JOptionPane.ERROR_MESSAGE);

		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}
