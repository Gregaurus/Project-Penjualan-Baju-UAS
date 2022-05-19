import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

public class payment extends JFrame implements ActionListener{

	private int totalPrice = 0, change, staffId;

	private JLabel lTotalPrice, lPayment;
	private JTextField tfPayment;
	private JPanel centerPanel, northPanel, southPanel;
	private JButton bPay;
	
	private boolean check;
	
	Connect con;

	public payment(int totalPrice, boolean check, int staffId) {
		this.check = check;
		this.totalPrice = totalPrice;
		this.staffId = staffId;
		setSize(300,120);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("PAYMENT");
		setLocationRelativeTo(null);
		init();
	}

	private void init() {
		con = new Connect();
		
		if (check) {
			int tempDiscount = totalPrice/10;
			totalPrice -= tempDiscount;
		}
		
		//NORTH
		String tempPrice = "Total Price : " +totalPrice;
		lTotalPrice = new JLabel(tempPrice);
		northPanel = new JPanel();
		northPanel.add(lTotalPrice);
		
		//CENTER 
		centerPanel = new JPanel(new GridLayout(1,2));
		lPayment = new JLabel("Payment : ");
		tfPayment = new JTextField();
		centerPanel.add(lPayment);
		centerPanel.add(tfPayment);
		
		//SOUTH
		southPanel = new JPanel();
		bPay = new JButton("PAY");
		bPay.addActionListener(this);
		southPanel.add(bPay);
		
		//ADD
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int tempPayment = Integer.parseInt(tfPayment.getText());
		change = tempPayment - totalPrice;
		
		if (change < 0) {
			JOptionPane.showMessageDialog(null, "Payment Not Enough", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			JOptionPane.showMessageDialog(null, "The Amount of Change is : " + change, "Message", JOptionPane.INFORMATION_MESSAGE);
			dispose();
			
			  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
			  LocalDate localDate = LocalDate.now();
			  String date = dtf.format(localDate); 
			  boolean insert = con.executeUpdate("INSERT INTO `detailtransaction` (`id`, `total_price`, `TransactionDate`, `staff_id`) VALUES (NULL, '"+totalPrice+"', '" +date+"', '"+staffId+ "')");
		}
	}
}