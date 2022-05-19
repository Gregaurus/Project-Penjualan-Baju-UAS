import java.awt.BorderLayout;
import java.awt.GridLayout;
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

public class salesEntry extends JInternalFrame implements ActionListener, MouseListener{

	private JPanel centerPanel, leftPanel, rightPanel, southPanel, northPanel;
	private JLabel lTotalPrice, lBlank;
	private JTable tRight, tLeft;
	private DefaultTableModel dtm1, dtm2;
	private JScrollPane sp1, sp2;
	private JButton bAdd, bRemove, bPay;
	
	private boolean clicked1 = false, clicked2 = false;
	private String name;
	private int price, totalPrice, staffId;
	
	Connect con;
	ResultSet rs;


	public salesEntry(int staffId) {
		this.staffId = staffId;
		setSize(1300, 700);
		setMaximizable(false);
		setIconifiable(true);
		setClosable(true);
		setTitle("Sales Entry");
		init();
	}

	private void init() {
		con = new Connect();

		tRight = new JTable();
		tRight.addMouseListener(this);
		tLeft = new JTable();
		tLeft.addMouseListener(this);
		Object[] columnNames2 = {"Item Name", "Price"};
		dtm2 = new DefaultTableModel(columnNames2, 0);
		tLeft.setModel(dtm2);

		refreshTable();
		
		//NORTH
		northPanel = new JPanel();
		bPay = new JButton("PAY");
		bPay.addActionListener(this);
		northPanel.add(bPay);

		//CENTER
		centerPanel = new JPanel(new GridLayout(1,2));

		//CENTER - LEFT
		leftPanel = new JPanel();
		sp2 = new JScrollPane(tLeft);
		leftPanel.add(sp2);

		//CENTER - RIGHT
		rightPanel = new JPanel();
		sp1 = new JScrollPane(tRight);
		rightPanel.add(sp1);
		
		centerPanel.add(leftPanel);
		centerPanel.add(rightPanel);
		
		//SOUTH
		southPanel = new JPanel(new GridLayout(2,2));
		lTotalPrice = new JLabel("Total Price: ");
		lBlank = new JLabel("");
		bAdd = new JButton("ADD TO CART");
		bAdd.addActionListener(this);
		bRemove = new JButton("REMOVE FROM CART");
		bRemove.addActionListener(this);
		southPanel.add(lTotalPrice);
		southPanel.add(lBlank);
		southPanel.add(bAdd);
		southPanel.add(bRemove);

		//ADD
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		pack();
	}
	
	private void refreshTotal() {
		lTotalPrice.setText("Total Price: " + totalPrice);
	}

	private void refreshTable() {
		Object[] columnNames = {"Item Name", "Image", "Availability", "Price"};

		dtm1 = new DefaultTableModel(columnNames, 0);

		rs = con.executeQuery("SELECT * FROM item");
		try {
			while(rs.next()) {
				String name = rs.getString("name");
				byte[] image = rs.getBytes("image");
				String availability = rs.getString("availability");
				int price = rs.getInt("price");


				//Scale down images
				ImageIcon tempImage = new ImageIcon(image);
				Image scaleImg = tempImage.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
				ImageIcon realImage = new ImageIcon(scaleImg);
				JLabel images = new JLabel(realImage);

				Vector<Object> rowData = new Vector<>();
				rowData.add(name);
				rowData.add(images);
				rowData.add(availability);
				rowData.add(price);

				dtm1.addRow(rowData);
			}
			tRight.setModel(dtm1);
			//render image
			tRight.getColumnModel().getColumn(1).setCellRenderer(new render());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//ADD TO CART
		if (e.getSource() == bAdd) {
			if (clicked1 == true) {
				name = tRight.getValueAt(tRight.getSelectedRow(), 0).toString();
				price = Integer.parseInt(tRight.getValueAt(tRight.getSelectedRow(), 3).toString());
				dtm2.addRow(new Object[] {name,price});
				totalPrice += price;
				refreshTotal();
				
				tRight.clearSelection();
				clicked1 = false;
			} else 
				JOptionPane.showMessageDialog(null, "Please Select from shop!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		if (e.getSource() == bRemove) {
			if (clicked2 == true) {
				dtm2.removeRow(tLeft.getSelectedRow());
				totalPrice -= price;
				refreshTotal();
				
				clicked2 = false;
			} else 
				JOptionPane.showMessageDialog(null, "Please Select from cart!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		if (e.getSource() == bPay) {
			boolean check = false;
			if (JOptionPane.showConfirmDialog(null, "Does the customer have a membership?", "Message", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				new membership().checkMembership(totalPrice, staffId);
			} else {
				new payment(totalPrice, check, staffId).setVisible(true);
			}
			dtm2.setRowCount(0);
			totalPrice = 0;
			refreshTotal();
			
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == tRight) {
			tLeft.clearSelection();
			clicked1 = true;
			clicked2 = false;
		}
		
		if (e.getSource() == tLeft) {
			tRight.clearSelection();
			price = Integer.parseInt(tLeft.getValueAt(tLeft.getSelectedRow(), 1).toString());
			clicked2 = true;
			clicked1 = false;
		}
		
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
