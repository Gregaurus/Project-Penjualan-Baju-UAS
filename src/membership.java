import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.*;

public class membership extends JFrame implements ActionListener{
	
	Connect con;
	public JTextField tfMember;
	private int totalPrice, staffId;
	
	public void checkMembership(int totalPrice, int staffId) {
		this.staffId = staffId;
		this.totalPrice = totalPrice;
		con = new Connect();
		
		setSize(300,120);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("checkMembership");
		setVisible(true);
		setLocationRelativeTo(null);
		
		JLabel lMember = new JLabel("Membership ID: ");
		tfMember = new JTextField();
		JPanel centerPanel = new JPanel(new GridLayout(1,2));
		
		centerPanel.add(lMember);
		centerPanel.add(tfMember);
		
		JButton confirm = new JButton("CONFIRM");
		confirm.addActionListener(this);
		
		JPanel southPanel = new JPanel();
		southPanel.add(confirm);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//CHECK
		ResultSet rs;
		String tempMember = tfMember.getText();
		boolean mTrue = false;
		
		try {
			String mCheck = "SELECT * FROM member";
			rs = con.executeQuery(mCheck);
			while (rs.next()) {
				String memberId = rs.getString("memberID");
				if (tempMember.equals(memberId)) {
					mTrue = true;
					break;
				}
			}
		} catch (Exception x) {
			x.getStackTrace();
		}
		if (mTrue) {
			JOptionPane.showMessageDialog(null, "Successful! You got 10% discount!", "Message", JOptionPane.INFORMATION_MESSAGE);
		} else 
			JOptionPane.showMessageDialog(null, "Wrong ID!", "Message", JOptionPane.INFORMATION_MESSAGE);
		new payment(totalPrice, mTrue, staffId).setVisible(true);
		dispose();
	}

}
