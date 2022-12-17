package bank;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;


import java.util.List;

import javax.swing.table.DefaultTableModel;


public class Front_End {
	
	public void rePaint(JFrame frame, JPanel f) {
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	//Home Page - Login Screen
	
	public void showLogin(JFrame frame, Users user) {
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc.png"));
			   
		imageLabel.setBounds(200,20,400, 200);
		f.add(imageLabel);
		
		
		JLabel uname = new JLabel("Username");
		uname.setBounds(250,280,200, 40);
		uname.setForeground(Color.white);
		
		f.add(uname);
		
		JLabel passwd = new JLabel("Password");
		passwd.setBounds(250,320,200, 40);
		passwd.setForeground(Color.white);
		f.add(passwd);
		
		
	
		JTextField unameText = new JTextField();
		unameText.setBounds(350,285,200, 25);
		f.add(unameText);
		
		JPasswordField passwdText = new JPasswordField();
		passwdText.setBounds(350,325,200, 25);
		f.add(passwdText);
			
		JButton button_login = new JButton("Log In");
		button_login.setFont( button_login.getFont().deriveFont(16f) );
		button_login.setBounds(350,380,100, 35);
		f.add(button_login);
		
		
		//login action
		button_login.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				
				Database db = new Database();
				String myPass = String.valueOf(passwdText.getPassword());
				Users user = db.logIn( unameText.getText(), myPass );
				if( user.getUserId().compareTo("") == 0 ) {
					JOptionPane.showMessageDialog(f, "The username and/or password is incorrect!!", "Login Failed", JOptionPane.ERROR_MESSAGE);
				}
				else {
					
					if( !user.isBanker() ) {
						//System.out.println("Hi");
						Account account = db.getAccount( user.getUserId() );
						if ( Integer.valueOf(account.getStatus()) == 0 ) 
							JOptionPane.showMessageDialog(f, "The Account has been closed!!", "Account Closed", JOptionPane.ERROR_MESSAGE);
						else if( Integer.valueOf(account.getStatus()) == 1 ) {
							frame.remove(f);
							frame.repaint();
							frame.validate();
							showUserMenu(frame, account);
						}
						else if( Integer.valueOf(account.getStatus()) == -1 ) {
							frame.remove(f);
							frame.repaint();
							frame.validate();
							showFirstLogin(frame, user);
						}
						else {
							JOptionPane.showMessageDialog(f, "Sorry!! Unable to log you in", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						Banker banker = new Banker();
						frame.remove(f);
						frame.repaint();
						frame.validate();
						showBankerMenu(frame, banker);
					}
				}		
			}
		});
		rePaint(frame, f);
	}
	
	
	
	// First time login Screen - Change default Password

	void showFirstLogin(JFrame frame, Users user)
	{
		
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Change Default Password Before Signing In");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		
		JLabel passwd1 = new JLabel("Password");
		passwd1.setBounds(200,200,200, 40);
		passwd1.setForeground(Color.white);
		
		f.add(passwd1);
		
		JLabel passwd2 = new JLabel("Confirm Password");
		passwd2.setBounds(200,240,200, 40);
		passwd2.setForeground(Color.white);
		f.add(passwd2);
		
		
	
		JPasswordField passwd1Text = new JPasswordField();
		passwd1Text.setBounds(350,205,200, 25);
		f.add(passwd1Text);
		
		JPasswordField passwd2Text = new JPasswordField();
		passwd2Text.setBounds(350,245,200, 25);
		f.add(passwd2Text);
			
		JButton button_change = new JButton("Change Password");
		button_change.setFont( button_change.getFont().deriveFont(16f) );
		button_change.setBounds(300,300,200, 35);
		f.add(button_change);
		
		JButton button_back = new JButton("Back");
		button_back.setFont( button_back.getFont().deriveFont(16f) );
		button_back.setBounds(350,360,100, 35);
		f.add(button_back);
		
		
		button_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showLogin(frame, user);
			}
		});
		
		button_change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String myPass1 = String.valueOf(passwd1Text.getPassword());
				String myPass2 = String.valueOf(passwd2Text.getPassword());
				
			
				int flag = user.changeDefaultPassword(myPass1, myPass2);
				if (flag != 1) {
					if(flag == -1) {
						JOptionPane.showMessageDialog(f, "Password cannot be empty!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else if(flag == -2) {
						JOptionPane.showMessageDialog(f, "Passwords do not match!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					frame.remove(f);
					frame.repaint();
					frame.validate();
					showFirstLogin(frame, user);
				}
				
				else {
					JOptionPane.showMessageDialog(f, "Password Chnaged Succesfull. You will be redirected to Login Screen", "Success", JOptionPane.INFORMATION_MESSAGE);
					frame.remove(f);
					frame.repaint();
					frame.validate();
					showLogin(frame, user);
				}
			}
		});
		
		
		

	}
	

	
	
	//User Home Scren after Log In
	
	void showUserMenu(JFrame frame, Account account) {
		
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel(account.getFname() + account.getLname());
		title.setBounds(0,40,800, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		JLabel balance = new JLabel("$" + account.getBalance());
		balance.setBounds(325,80,150, 40);
		balance.setForeground(Color.blue);
		balance.setBackground(Color.yellow);
		balance.setHorizontalAlignment(SwingConstants.CENTER);
		balance.setFont( balance.getFont().deriveFont(20f) );
		balance.setOpaque(true);

		f.add(balance);
		
		
		
		
		

		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Main Menu");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,130, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showUserMenu(frame, account);
			}
		});
	
		
		// Transfer Money Button
		JButton button_transfer = new JButton("Transfer Funds");
		button_transfer.setBounds(250,200,300, 40);
		f.add(button_transfer);
		
		// Phone Bill Button
		JButton button_bill = new JButton("Electricity Bill");
		button_bill.setBounds(250,250,300, 40);
		f.add(button_bill);
	
		// Mini Statement Button
		JButton button_trans = new JButton("Last 10 Transaction");
		button_trans.setBounds(250,300,300, 40);
		f.add(button_trans);
		
		// Mini Statement Button
		JButton button_change = new JButton("Change Password");
		button_change.setBounds(250,350,300, 40);
		f.add(button_change);
		
		
		
		// Transfer action
		button_transfer.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    showTransfer(frame, account);
			}
		});
		
		// Transfer action
		button_bill.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    showBill(frame, account);
			}
		});
		
		// Last 10 Transactions action
		button_trans.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    showTransactions(frame, account);
			}
		});
		
	
		// Change Password action
		button_change.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    showChangePassword(frame, account);
			}
		});	
		
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	//Change Password Screen
	void showChangePassword(JFrame frame, Account account)
	{
		
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Change Password");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		
		JLabel passwd1 = new JLabel("Password");
		passwd1.setBounds(200,200,200, 40);
		passwd1.setForeground(Color.white);
		
		f.add(passwd1);
		
		JLabel passwd2 = new JLabel("Confirm Password");
		passwd2.setBounds(200,240,200, 40);
		passwd2.setForeground(Color.white);
		f.add(passwd2);
		
		
	
		JPasswordField passwd1Text = new JPasswordField();
		passwd1Text.setBounds(350,205,200, 25);
		f.add(passwd1Text);
		
		JPasswordField passwd2Text = new JPasswordField();
		passwd2Text.setBounds(350,245,200, 25);
		f.add(passwd2Text);
			
		JButton button_change = new JButton("Change Password");
		button_change.setFont( button_change.getFont().deriveFont(16f) );
		button_change.setBounds(300,300,200, 35);
		f.add(button_change);
		

		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Main Menu");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,130, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showUserMenu(frame, account);
			}
		});
		
		button_change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String myPass1 = String.valueOf(passwd1Text.getPassword());
				String myPass2 = String.valueOf(passwd2Text.getPassword());
				
			
				int flag = account.changePassword(myPass1, myPass2);
				if (flag != 1) {
					if(flag == -1) {
						JOptionPane.showMessageDialog(f, "Password cannot be empty!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else if(flag == -2) {
						JOptionPane.showMessageDialog(f, "Passwords do not match!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					frame.remove(f);
					frame.repaint();
					frame.validate();
					showChangePassword(frame, account);
				}
				
				else {
					JOptionPane.showMessageDialog(f, "Password Chnaged Succesfull. You will be redirected to Login Screen", "Success", JOptionPane.INFORMATION_MESSAGE);
					frame.remove(f);
					frame.repaint();
					frame.validate();
					Users user = new Users();
					showLogin(frame, user);
				}
			}
		});
		
		
		

	}
	
	
	
	//Transactions Screen
	void showTransactions(JFrame frame, Account account)
	{
		
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Last 10 Transactions");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		
		// creating table for transaction data
		DefaultTableModel tableModel = new DefaultTableModel();
		JTable table=new JTable(tableModel);   
		tableModel.addColumn("TID");
		tableModel.addColumn("Date");
		tableModel.addColumn("Time");
		tableModel.addColumn("Amount");
		tableModel.addColumn("Type");
		tableModel.addColumn("Remarks");
		
		table.setEnabled(false);
		JScrollPane sp=new JScrollPane(table);
		sp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		sp.setBounds(50,230,700,150);  
		f.add(sp);
		sp.setVisible(true);
		
		JButton button_transactions = new JButton("Fetch Transactions");
		button_transactions.setFont( button_transactions.getFont().deriveFont(16f) );
		button_transactions.setBounds(300,150,200, 35);
		f.add(button_transactions);
		
		
		button_transactions.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.setRowCount(0);
				//sp.setVisible(false);
				
				List<Transactions> list = account.getTransactions( account.getAccountNum());
				if( list.size() > 0 ) {
					for( Transactions th: list) {
						tableModel.addRow(new Object[] { th.getSerialNo(), th.getAmount(), th.getType(), th.getDate(), th.getTime(), th.getRefNo() });
					}	
					tableModel.fireTableDataChanged();
					sp.setVisible(true);
					
				}
				
			}
		});
		
		
		
		
		
		
		
		

		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Main Menu");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,130, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showUserMenu(frame, account);
			}
		});
		


				
	
		
		

	}
	
	
	//Transfer Screen
	
	
	
	
	// Transfer Screen
	void showTransfer(JFrame frame, Account account) {
		
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Transfer Funds (within bank)");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		JLabel subTitle = new JLabel("Balance Available : $" + account.getBalance());
		subTitle.setBounds(15,120,785, 40);
		subTitle.setForeground(Color.white);
		subTitle.setHorizontalAlignment(SwingConstants.CENTER);
		subTitle.setFont( subTitle.getFont().deriveFont(16f) );
		f.add(subTitle);
		
		
		// Receiver Account Number 
		JLabel to_acc_no = new JLabel("To Account Number: ");
		to_acc_no.setBounds(200,200,200, 40);
		f.add(to_acc_no);
		
		// Amount Label
		JLabel amount = new JLabel("Amount: ");
		amount.setBounds(200,240,200, 40);
		f.add(amount);
	
		// Receiver Account Number Text Field
		JTextField accText = new JTextField();
		accText.setBounds(400,200,180, 25);
		f.add(accText);
		
		// Amount Text field
		JTextField amtText = new JTextField();
		amtText.setBounds(400,240,180, 25);
		f.add(amtText);
		
		
		
		// Transfer Button
		JButton button_transfer = new JButton("Transfer");
		button_transfer.setFont( button_transfer.getFont().deriveFont(16f) );
		button_transfer.setBounds(300,300,200, 35);
		f.add(button_transfer);
		
		
		// Transfer Action
		button_transfer.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if( accText.getText().compareTo( account.getAccountNum() ) == 0  )
					JOptionPane.showMessageDialog(f, "Enter an account number other than your own", "Warning", JOptionPane.WARNING_MESSAGE);
				else if (accText.getText().compareTo("1001") == 0)
					JOptionPane.showMessageDialog(f, "Invalid Account Number!! (This number is reserved for banker.)", "Warning", JOptionPane.WARNING_MESSAGE);
				else {
					int r = account.transferMoney( accText.getText(), Integer.parseInt( amtText.getText() ));
					if( r == 1 )
						JOptionPane.showMessageDialog(f, "Account does not exist!!", "Warning", JOptionPane.WARNING_MESSAGE);
					else if( r == 2)
						JOptionPane.showMessageDialog(f, "Insufficient Balance", "Warning", JOptionPane.WARNING_MESSAGE);

					else if( r == 3 ) {
						JOptionPane.showMessageDialog(f, "Amount Transferred", "Success", JOptionPane.INFORMATION_MESSAGE);
						account.updateBalance();
						frame.remove(f);
						frame.repaint();
						frame.validate();
					    showUserMenu(frame, account);
					}
					else if( r == 4)
						JOptionPane.showMessageDialog(f, "Insufficient Balance. You must maintain at least 500 balance.", "Warning", JOptionPane.WARNING_MESSAGE);

					else { 
						
					}
				}
			}
		});
		
		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Home");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,100, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showUserMenu(frame, account);
			}
		});
		
	
		
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);	
	}
	
	

	
	
	//Phone Recharge Screen
	void showBill(JFrame frame, Account account) {
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Pay Electricity & Water Bill");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		JLabel subTitle = new JLabel("Balance Available : $" + account.getBalance());
		subTitle.setBounds(15,120,785, 40);
		subTitle.setForeground(Color.white);
		subTitle.setHorizontalAlignment(SwingConstants.CENTER);
		subTitle.setFont( subTitle.getFont().deriveFont(16f) );
		f.add(subTitle);
		
		
		// Customer ID 
		JLabel to_acc_no = new JLabel("Conection ID: ");
		to_acc_no.setBounds(200,200,200, 40);
		f.add(to_acc_no);
		
		// Amount Label
		JLabel amount = new JLabel("Amount: ");
		amount.setBounds(200,240,200, 40);
		f.add(amount);
	
		// Receiver Account Number Text Field
		JTextField accText = new JTextField();
		accText.setBounds(400,200,180, 25);
		f.add(accText);
		
		// Amount Text field
		JTextField amtText = new JTextField();
		amtText.setBounds(400,240,180, 25);
		f.add(amtText);
		
		
		
		// Transfer Button
		JButton button_recharge = new JButton("Recharge");
		button_recharge.setFont( button_recharge.getFont().deriveFont(16f) );
		button_recharge.setBounds(300,300,200, 35);
		f.add(button_recharge);
		
		
		// Transfer Action
		button_recharge.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				int r = account.payBill( accText.getText(), Integer.parseInt( amtText.getText() ));
			    if( r == 2)
					JOptionPane.showMessageDialog(f, "Insufficient Balance", "Warning", JOptionPane.WARNING_MESSAGE);
				else if( r == 3 ) {
					JOptionPane.showMessageDialog(f, "Bill Payment Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
					account.updateBalance();
					frame.remove(f);
					frame.repaint();
					frame.validate();
				    showUserMenu(frame, account);
				}
				else if( r == 4)
					JOptionPane.showMessageDialog(f, "Insufficient Balance. You must maintain at least 500 balance.", "Warning", JOptionPane.WARNING_MESSAGE);

				else { 
					
				}
			}
		});
		
		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Home");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,100, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showUserMenu(frame, account);
			}
		});
		
	
		
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);	
	}
	

	
	// BANKER MENU SCREEN
	void showBankerMenu(JFrame frame, Banker banker) {
		
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Bank Clerk");
		title.setBounds(0,40,800, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		

		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Main Menu");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,130, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showBankerMenu(frame, banker);
			}
		});
	
		
	
		JButton button_createAccount = new JButton("Create Account");
		button_createAccount.setBounds(250,200,300, 40);
		f.add(button_createAccount);
		
		
		JButton button_searchAccount = new JButton("Search Account");
		button_searchAccount.setBounds(250,250,300, 40);
		f.add(button_searchAccount);
	
		JButton button_closeAccount = new JButton("Close Account");
		button_closeAccount.setBounds(250,300,300, 40);
		f.add(button_closeAccount);
		
		
		JButton button_deposit = new JButton("Cash Deposit");
		button_deposit.setBounds(250,350,300, 40);
		f.add(button_deposit);
		
		
		JButton button_withdraw = new JButton("Cash Withdrawl");
		button_withdraw.setBounds(250,400,300, 40);
		f.add(button_withdraw);
		
		
		
		// Create Account action
		button_createAccount.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    showCreateAccount(frame, banker);
			}
		});
		
		// Search Account action
		button_searchAccount.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    showDisplayAccount(frame, banker);
			}
		});
		
		// Close Account action
		button_closeAccount.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
			    showCloseAccount(frame, banker);
			}
		});
		
		// Deposit Cash action
		button_deposit.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showSearchAccount(frame, banker, 1);
			}
		});
			
		
		// Deposit Cash action
		button_withdraw.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showSearchAccount(frame, banker, 2);
			}
		});
		
		
		
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	
	
	// screen to create a new account
	public void showCreateAccount(JFrame frame, Banker banker) {
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Create a Bank Account");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Home");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,100, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showBankerMenu(frame, banker);
			}
		});
		
		
		// Account Fields Labels
		JLabel fname = new JLabel("First Name: ");
		fname.setForeground(Color.white);
		fname.setBounds(100,200,100, 40);
		f.add(fname);
		JLabel lname = new JLabel("Last Name: ");
		lname.setForeground(Color.white);
		lname.setBounds(400,200,100, 40);
		f.add(lname);
		
		JLabel phone = new JLabel("Phone: ");
		phone.setForeground(Color.white);
		phone.setBounds(100,250,100, 40);
		f.add(phone);
		JLabel email = new JLabel("Email: ");
		email.setForeground(Color.white);
		email.setBounds(400,250,100, 40);
		f.add(email);
		
		JLabel acc_type = new JLabel("Account Type: ");
		acc_type.setForeground(Color.white);
		acc_type.setBounds(100,300,100, 40);
		f.add(acc_type);
		
		// Account Text Fields
		JTextField fnameTxt = new JTextField();
		fnameTxt.setBounds(200,210,180, 25);
		f.add(fnameTxt);
		JTextField lnameTxt = new JTextField();
		lnameTxt.setBounds(500,210,180, 25);
		f.add(lnameTxt);

		JTextField phoneTxt = new JTextField();
		phoneTxt.setBounds(200,260,180, 25);
		f.add(phoneTxt);
		JTextField emailTxt = new JTextField();
		emailTxt.setBounds(500,260,180, 25);
		f.add(emailTxt);
		
		// Account Type
		String[] types = {"current", "savings" };
		@SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox jcb_types = new JComboBox(types);
		jcb_types.setBounds(200,310,180, 25);
		f.add(jcb_types);
			
		// Create Button
		JButton btn_create = new JButton("Create");
		btn_create.setBounds(580,310,100, 40);
		f.add(btn_create);
	
		// function to be executed when Create Button is clicked
		btn_create.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String acc_type_text = jcb_types.getSelectedItem().toString();
				int initial_balance = 0;
				if(acc_type_text.equals("savings")) {
					initial_balance = 500; 
				}
				
				Account newAccount = new Account( "",
						fnameTxt.getText(), lnameTxt.getText(), acc_type_text, String.valueOf(initial_balance), "-1",
						phoneTxt.getText(), emailTxt.getText()
						);
				int res =  banker.createAccount(newAccount);
				if( res == 1 ) {
					String id_ = banker.getAccountNum( phoneTxt.getText() );
					String uname = banker.createUser(fnameTxt.getText(), id_);
					String msg = "Account created";
					if( id_.compareTo("") != 0 )
						msg += " with Account Number: "+id_+", username: "+uname+" and Password: 1234";
					JOptionPane.showMessageDialog(f, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
					frame.remove(f);
					frame.repaint();
					frame.validate();
				    showBankerMenu(frame, banker);
				}
				else if( res == 2 )
					JOptionPane.showMessageDialog(f, "Another account with this Phone number exists");
				else
					JOptionPane.showMessageDialog(f, "Account creation failed");			
			}
		});
		
		
		
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	

		
	// close account screen
	void showCloseAccount(JFrame frame, Banker banker)
	{
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Close an Account");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
	
		

		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Home");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,100, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showBankerMenu(frame, banker);
			}
		});
		
		
		
		
		JLabel acc_num = new JLabel("Account No:");
		acc_num.setForeground(Color.white);
		acc_num.setBounds(220,200,150, 90);
		f.add(acc_num);
		
		
		JTextField accTxt = new JTextField();
		accTxt.setBounds(400, 230 ,200, 25);
		f.add(accTxt);

		JButton btn_close_account = new JButton("Close Account");
		btn_close_account.setBounds(325,280,150, 40);
		f.add(btn_close_account);

		// function to be executed when close account button is clicked
		btn_close_account.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int close_status = banker.close_account(accTxt.getText());
				
				if ( close_status == 0 )
				{
					JOptionPane.showMessageDialog(f, "Account has been successfully closed");
					showBankerMenu(frame, banker);
				}
				else
				{
					JOptionPane.showMessageDialog(f, "Account number does not exist!");
					showBankerMenu(frame, banker);
				}
			}
		});
		
		
	}
	
	
	
	
	
	
	// In this screen manager will enter the account's account number and SIN number for search
	void showDisplayAccount(JFrame frame, Banker banker) {
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Search Account");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);

		// Account Number Label
		JLabel acc_num = new JLabel("Enter account number:");
		acc_num.setBounds(150,150,150, 40);
		title.setForeground(Color.white);
		f.add(acc_num);
		
		// Account Number Text Field
		JTextField acc_numTxt = new JTextField();
		acc_numTxt.setBounds(150,190,180, 25);
		f.add(acc_numTxt);

		// Account Name Label
		JLabel name = new JLabel();
		name.setBounds(150,230,250, 40);
		name.setForeground(Color.white);
		name.setVisible(false);
		f.add(name);

		// Account Account Number
		JLabel acc_number = new JLabel();
		acc_number.setBounds(150,270,250, 40);
		acc_number.setForeground(Color.white);
		acc_number.setVisible(false);
		f.add(acc_number);
		
		// Account Account Type
		JLabel acc_type = new JLabel();
		acc_type.setBounds(150,310,250, 40);
		acc_type.setForeground(Color.white);
		acc_type.setVisible(false);
		f.add(acc_type);
		
		// Account Account Balance
		JLabel balance = new JLabel();
		balance.setBounds(150,350,250, 40);
		balance.setVisible(false);
		balance.setForeground(Color.white);
		f.add(balance);
		
		// Account Status Balance
		JLabel status = new JLabel();
		status.setBounds(150,400,250, 40);
		status.setVisible(false);
		status.setForeground(Color.white);
		f.add(status);

		// Search Account Button
		JButton button_search = new JButton("Search");
		button_search.setBounds(350,130,110, 40);
		f.add(button_search);
				
		// function to execute with clicked on Search Button
		button_search.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Account account = banker.getAccountInfo( acc_numTxt.getText() );
				
				
				// check if either account or account was not found
				if( (account.getAccountNum().compareTo("") == 0)) {
					
					name.setVisible(false);
					
					acc_number.setVisible(false);
					acc_type.setVisible(false);
					balance.setVisible(false);
					
					
					// display a message box that account was not found
					if(account.getAccountNum().compareTo("") == 0) {
						JOptionPane.showMessageDialog(f, "Account not found");
					}
					
				
					account = null;
				}
				
				// Both account and his account was found
				else {
		
					name.setText("Name:  "+account.getFname()+" "+account.getLname() );
					
					acc_number.setText("Account No:  "+account.getAccountNum() );
					acc_type.setText("Account Type:  "+account.getType() );
					balance.setText("Balance:  "+account.getBalance() );
					String temp = "Open";
					if( Integer.valueOf( account.getStatus() ) ==  0 )
						temp = "Close";
					else if( Integer.valueOf( account.getStatus() ) ==  2 )
						temp = "Block";
					status.setText("Status:  "+ temp );
					
					name.setVisible(true);
				
					acc_number.setVisible(true);
					acc_type.setVisible(true);
					balance.setVisible(true);
					status.setVisible(true);
					
				}
			}
		});
		
		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Home");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,100, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showBankerMenu(frame, banker);
			}
		});
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	
	
	
	
	
	// SEARCH ACCOUNT
	// to search him to deposit money
	void showSearchAccount(JFrame frame, Banker banker, int op) {
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
	
		
		rePaint(frame, f);
		

		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Home");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,100, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showBankerMenu(frame, banker);
			}
		});
		
		
		JLabel title = new JLabel("Deposit Money");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		if( op ==  2 )
			title.setText("Withdraw Money");
		
		
		// Account Number Label
		JLabel acc_num = new JLabel("Account number");
		acc_num.setForeground(Color.white);
		acc_num.setHorizontalAlignment(SwingConstants.CENTER);
		acc_num.setBounds(330,150,150, 40);
		f.add(acc_num);
		
		// Account Number Text Field
		JTextField accTxt = new JTextField();
		accTxt.setBounds(330,190,150, 25);
		f.add(accTxt);
		
		
		
		// Search Account Button
		JButton button_search = new JButton("Search");
		button_search.setBounds(350,270,110, 40);
		f.add(button_search);
				
		// function to execute with clicked on Search Button
		button_search.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Account account = banker.getAccountInfo( accTxt.getText() );
			
				// check if either account or account was not found
				if( (account.getAccountNum().compareTo("") == 0)) {
					
					
						JOptionPane.showMessageDialog(f, "Account not found");
					account = null;
				}
				
				else {		
					if( Integer.valueOf( account.getStatus() ) == 0 ) {
						JOptionPane.showMessageDialog(f, "Account is closed");
					}
					
					else {
						frame.remove(f);
						frame.repaint();
						frame.validate();
						if( op == 1 )
							showDepositCash(frame, banker, account);
						else if( op == 2 )
							showWithdrawCash(frame, banker, account);
						
					}
				}
				
			}
		});
		
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	
	
	// deposit case screen
	void showDepositCash(JFrame frame, Banker banker, Account account) {
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
		
		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Home");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,100, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showBankerMenu(frame, banker);
			}
		});
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Deposit Cash");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		
		
		// Name Label
		JLabel toName = new JLabel( "Account Name: " + account.getFname() + " " + account.getLname() );
		toName.setForeground(Color.white);
		toName.setBounds(150,150,350, 40);
		f.add(toName);
		
		
		// Account Number Label
		JLabel toAccNum = new JLabel( "Account Number: " + account.getAccountNum() );
		toAccNum.setForeground(Color.white);
		toAccNum.setBounds(150,190,400, 40);
		f.add(toAccNum);
		
		
		// Amount Label
		JLabel amt = new JLabel("Enter amount: ");
		amt.setForeground(Color.white);
		amt.setBounds(150,220,150, 40);
		f.add(amt);
		
		// Amount Text Field
		JTextField amtTxt = new JTextField();
		amtTxt.setBounds(150,260,180, 25);
		f.add(amtTxt);
		
		// Deposit Button
		JButton button_deposit = new JButton("Deposit");
		button_deposit.setBounds(150,320,100, 30);
		f.add(button_deposit);
		
		// function to be executed when Deposit Button is clicked
		button_deposit.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				if( amtTxt.getText().compareTo("") != 0 ) {
					if( Integer.valueOf( amtTxt.getText()) > 0 ) {
						// add amount to account
						int r = account.addAmount( Integer.valueOf( amtTxt.getText()) );
						if( r == 1 ) {
							// show deposit successful message
							JOptionPane.showMessageDialog(f, "Amount deposited successfully");
						}
						else {
							JOptionPane.showMessageDialog(f, "Amount deposit failed");
						}
						
						// go back to menu of accountant
						frame.remove(f);
						frame.repaint();
						frame.validate();
						showBankerMenu(frame, banker);
					}
					// invalid amount
					else {
						JOptionPane.showMessageDialog(f, "Invalid Amount");
					}
				}
				// invalid amount
				else {
					JOptionPane.showMessageDialog(f, "Invalid Amount");
				}
			}
		});
		
	
		
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	
	// Withdraw case screen
	void showWithdrawCash(JFrame frame, Banker banker, Account account) {
		JPanel f = new JPanel();		
		f.setBackground(new Color(0, 102, 102));
		JLabel imageLabel1 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_left.png"));   
		imageLabel1.setBounds(5,5,130, 130);
		f.add(imageLabel1);
		
		JLabel imageLabel2 = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bank/src/bank/rbc_logo_top_right.png"));   
		imageLabel2.setBounds(695,5,100, 130);
		f.add(imageLabel2);
		
		JButton button_logout = new JButton("Logout");
		button_logout.setFont( button_logout.getFont().deriveFont(14f) );
		button_logout.setBounds(695,150,100, 30);
		f.add(button_logout);
		
		// Log Out action
		button_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				Users user = new Users ();
				showLogin(frame, user);
			}
		});
		
		JButton home = new JButton("Home");
		home.setFont( home.getFont().deriveFont(14f) );
		home.setBounds(5,150,100, 30);
		f.add(home);
		
		// Home action
		home.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(f);
				frame.repaint();
				frame.validate();
				showBankerMenu(frame, banker);
			}
		});
	
		
		rePaint(frame, f);
		
		
		JLabel title = new JLabel("Withdraw Cash");
		title.setBounds(15,80,785, 40);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont( title.getFont().deriveFont(20f) );
		f.add(title);
		
		
		
		// Name Label
		JLabel toName = new JLabel( "Account Name: " + account.getFname() + " " + account.getLname() );
		toName.setForeground(Color.white);
		toName.setBounds(150,150,350, 40);
		f.add(toName);
		
		
		// Account Number Label
		JLabel toAccNum = new JLabel( "Account Number: " + account.getAccountNum() );
		toAccNum.setForeground(Color.white);
		toAccNum.setBounds(150,180,400, 40);
		f.add(toAccNum);
		
		// Account Number Label
		JLabel balanceLabel = new JLabel( "Balance: $" + account.getBalance() );
		balanceLabel.setForeground(Color.white);
		balanceLabel.setBounds(150,210,400, 40);
		f.add(balanceLabel);
		
		
		// Amount Label
		JLabel amt = new JLabel("Enter amount: ");
		amt.setBounds(150,250,150, 40);
		amt.setForeground(Color.white);
		f.add(amt);
		
		// Amount Text Field
		JTextField amtTxt = new JTextField();
		amtTxt.setBounds(150,290,180, 25);
		f.add(amtTxt);
		
		// Deposit Button
		JButton button_deposit = new JButton("Withdraw");
		button_deposit.setBounds(150,350,100, 30);
		f.add(button_deposit);
		
		// function to be executed when Deposit Button is clicked
		button_deposit.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				if( amtTxt.getText().compareTo("") != 0 ) {
					if( Integer.valueOf( amtTxt.getText()) > 0 ) {
						// add amount to account
						int r = account.removeAmount( Integer.valueOf( amtTxt.getText()) );
						if( r == 1 ) {
							// show deposit successful message
							JOptionPane.showMessageDialog(f, "Amount Withdrawn successfully");
						}
						else if(r==2) {
							JOptionPane.showMessageDialog(f, "Insufficient Balance");
						}
						else if(r==3) {
							JOptionPane.showMessageDialog(f, "Insufficient Balance. Minimum Balance in Savings account must be 500.");
						}
						
						
						frame.remove(f);
						frame.repaint();
						frame.validate();
						showBankerMenu(frame, banker);
					}
					// invalid amount
					else {
						JOptionPane.showMessageDialog(f, "Invalid Amount");
					}
				}
				// invalid amount
				else {
					JOptionPane.showMessageDialog(f, "Invalid Amount");
				}
			}
		});
		
	
		
		
		// displaying the new panel on frame	
		f.setLayout(null); 
		frame.setContentPane(f);
		frame.setVisible(true);
	}
	
	
	

	
	


}
