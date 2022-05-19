import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener{

	private JDesktopPane dp;
	private JMenuBar mb;
	private JMenu Sales, User;
	private JMenuItem salesEntry, updateSalesEntry, logOff;

	private salesEntry se;
	private updateSalesEntry use;

	private int staffId;
	
	Connect con;
	ResultSet rs;
	

	public MainFrame(int staffId) {
		this.staffId = staffId;
		setSize(1400,1000);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("HOME");
		setLocationRelativeTo(null);
		init();
	}

	private void init() {
		con = new Connect();
		dp = new JDesktopPane();
		setContentPane(dp);
		mb = new JMenuBar();

		Sales = new JMenu("Sales");

		salesEntry = new JMenuItem("Sales Entry");
		salesEntry.addActionListener(this);
		updateSalesEntry = new JMenuItem("Update Sales Entry");
		updateSalesEntry.addActionListener(this);
		Sales.add(salesEntry);
		Sales.add(updateSalesEntry);

		User = new JMenu("User");
		logOff = new JMenuItem("Log Off");
		logOff.addActionListener(this);
		User.add(logOff);

		mb.add(Sales);
		mb.add(User);
		setJMenuBar(mb);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == salesEntry) {
			se = new salesEntry(staffId);
			se.setVisible(true);
			dp.add(se);
		} else if (e.getSource() == updateSalesEntry) {
			use = new updateSalesEntry();
			use.setVisible(true);
			dp.add(use);
		} else if (e.getSource() == logOff) {
			int totalTransaction=0, totalSales=0;
			String name="";
			//show transaction count and total for todays date and the staff.
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
			LocalDate localDate = LocalDate.now();
			String date = dtf.format(localDate); 
			System.out.println(date);
			//check count transactions
			String query = "SELECT COUNT(*) AS COUNT FROM detailtransaction WHERE TransactionDate = '" + date +"' AND staff_ID = " +staffId;
			rs = con.executeQuery(query);
			try {
				rs.next();
				totalTransaction = rs.getInt("COUNT");
			} catch (Exception x) {
				x.getStackTrace();
			}
			
			//check sum total price
			String query2 = "SELECT SUM(total_price) AS SUM FROM detailtransaction WHERE TransactionDate = '" + date +"' AND staff_ID = " +staffId;
			rs = con.executeQuery(query2);
			try {
				rs.next();
				totalSales = rs.getInt("SUM");
			} catch (Exception x) {
				x.getStackTrace();
			}
			
			//grab name
			String query3 = "SELECT username FROM staff WHERE id = " + staffId;
			rs = con.executeQuery(query3);
			try {
				rs.next();
				name = rs.getString("username");
			} catch (Exception x) {
				x.getStackTrace();
			}
			
			if (JOptionPane.showConfirmDialog(null , "Total Transactions: " + totalTransaction + ", Total Sales: Rp. " + totalSales, name + "'s SHIFT", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				boolean temp = false;
				String tempPass = JOptionPane.showInputDialog(null, "Password: ", "Supervisor Password", JOptionPane.PLAIN_MESSAGE);
				String supQuery = "SELECT * FROM staff WHERE role = 2";
				rs = con.executeQuery(supQuery);
				try {
					while (rs.next()) {
						String password = rs.getString("password");
						if (tempPass.equals(password)) {
							temp = true;
							break;
						}
					}
				} catch (Exception x) {
					x.getStackTrace();
				}
				if (temp) {
					JOptionPane.showMessageDialog(null, "Successfully logged out!", "Message", JOptionPane.INFORMATION_MESSAGE);
					new loginPage().setVisible(true);
					dispose();
				} else
					JOptionPane.showMessageDialog(null, "Wrong Password!", "Message", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
