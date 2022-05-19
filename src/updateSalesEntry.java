import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class updateSalesEntry extends JInternalFrame implements ActionListener, MouseListener{
	
	private JPanel northPanel, centerPanel, southPanel;
	private JLabel lPrice;
	private JTable table;
	private DefaultTableModel dtm;
	private JScrollPane sp;
	private JTextField tfPrice;
	private JButton bUpdate, bDelete, bCall;
	
	Connect con;
	ResultSet rs;
	
	private String showPrice, tempId;
	private boolean clicked = false;

	public updateSalesEntry() {
		setSize(1300, 700);
		setMaximizable(false);
		setIconifiable(true);
		setClosable(true);
		setTitle("Sales Entry Database");
		init();
	}
	
	private void init() {
		con = new Connect();
		table = new JTable();
		table.addMouseListener(this);
		
		refreshTable();
		
		//NORTH
		northPanel = new JPanel();
		bCall = new JButton("Call Supervisor");
		bCall.addActionListener(this);
		northPanel.add(bCall);
		
		//CENTER
		centerPanel = new JPanel();
		sp = new JScrollPane(table);
		centerPanel.add(sp);
		
		//SOUTH
		southPanel = new JPanel(new GridLayout(2,2,20,30));
		lPrice = new JLabel("Total Price: ");
		tfPrice = new JTextField();
		bUpdate = new JButton("UPDATE");
		bUpdate.addActionListener(this);
		bDelete = new JButton("DELETE");
		bDelete.addActionListener(this);
		southPanel.add(lPrice);
		southPanel.add(tfPrice);
		southPanel.add(bUpdate);
		southPanel.add(bDelete);
		
		
		//ADD
		add(centerPanel, BorderLayout.CENTER);
		add(northPanel, BorderLayout.NORTH);
		add(southPanel, BorderLayout.SOUTH);
		pack();
		
	}

	private void refreshTable() {
		Object[] columnNames = {"Transaction ID", "Total Price", "Staff Name"};

		dtm = new DefaultTableModel(columnNames, 0);

		rs = con.executeQuery("SELECT * FROM detailtransaction JOIN staff ON detailtransaction.staff_id = staff.id");
		try {
			while(rs.next()) {
				int id = rs.getInt(1);
				int totalPrice = rs.getInt("total_price");
				String staffName = rs.getString("username");

				Vector<Object> rowData = new Vector<>();
				rowData.add(id);
				rowData.add(totalPrice);
				rowData.add(staffName);

				dtm.addRow(rowData);
			}
			table.setModel(dtm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bUpdate) {
			//UPDATE QUERY
			boolean pCorrect = false;
			if (clicked == false) {
				JOptionPane.showMessageDialog(null, "Please select a data!", "ERROR", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				pCorrect = passCheck();
			}
			if (pCorrect) {
				String tempPrice = tfPrice.getText();
				String query = "UPDATE `detailtransaction` SET `total_price` = '"+tempPrice+"' WHERE `detailtransaction`.`id` = " +tempId;
				boolean update = con.executeUpdate(query);
				JOptionPane.showMessageDialog(null,  "Successfully updated!", "Message", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null,  "Password is Incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
			table.clearSelection();
			clicked = false;
			
			refreshTable();
		}
		
		if (e.getSource() == bCall) {
			//CALL BUTTON
			if (JOptionPane.showConfirmDialog(null, "Do you want to call Supervisor?", "Message", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "Supervisor successfully called!", "Message", JOptionPane.INFORMATION_MESSAGE);
			} else 
				JOptionPane.showMessageDialog(null, "Action Cancelled!", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
		}
		
		if (e.getSource() == bDelete) {
			//DELETE QUERY
			boolean pCorrect = false;
			if (clicked == false) {
				JOptionPane.showMessageDialog(null, "Please select a data!", "ERROR", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				pCorrect = passCheck();
			}
			if (pCorrect) {
				String query = "DELETE FROM detailtransaction WHERE detailtransaction.id = " +tempId;
				boolean update = con.executeUpdate(query);
				JOptionPane.showMessageDialog(null,  "Successfully deleted!", "Message", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null,  "Password is Incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
			clicked = false;
			
			refreshTable();
		}
		
		
	}

	private boolean passCheck() {
		boolean temp = false;
		String tempPass = JOptionPane.showInputDialog(null, "Password: ");
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
		return temp;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		showPrice = table.getValueAt(table.getSelectedRow(), 1).toString();
		tfPrice.setText(showPrice);
		tempId = table.getValueAt(table.getSelectedRow(), 0).toString();
		clicked = true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
