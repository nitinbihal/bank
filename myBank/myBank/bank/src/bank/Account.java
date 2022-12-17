package bank;

import java.util.List;


public class Account {
	private String account_number;
	private String fname;
	private String lname;
	private String type;
	private String balance;
	private String status;
	private String phone;
	private String email;
	

	Account() {
		account_number = "";
		fname = "";
		lname = "";
		type = "";
		balance = "";
		status = "";
		phone = "";
		email = "";	
	}
	
	
	Account(String account_number, String fname, String lname, String type,
			String balance, String status, String phone, String email) {
		this.account_number = account_number;
		this.fname = fname;
		this.lname = lname;
		this.type = type;
		this.balance = balance;
		this.status = status;
		this.phone= phone;
		this.email= email;
	}
	

	public void addInfo(String account_number, String fname, String lname, String type,
			String balance, String status, String phone, String email) {
		this.account_number = account_number;
		this.fname = fname;
		this.lname = lname;
		this.type = type;
		this.balance = balance;
		this.status = status;
		this.phone= phone;
		this.email= email;
		
	}
	
	// setter function for fname
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	// setter function for lname
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	// setter function for type
	public void setType(String type) {
		this.type = type;
	}
	
	// setter function for balance
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	// setter function for balance
	public void setStatus(String status) {
		this.status = status;
	}
	
	// setter function for phone
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	// setter function for email
	public void setEmail(String email) {
		this.email = email;
	}
	
	// getter function for email
	public String getAccountNum() {
		return this.account_number;
	}
	
	
	public String getUserId() {
		return this.account_number;
	}
	
	// getter function for type
	public String getType() {
		return this.type;
	}
	
	// getter function for balance
	public String getBalance() {
		return this.balance;
	}

	// getter function for status
	public String getStatus() {
		return this.status;
	}
	
	// getter function for phone
	public String getPhone() {
		return this.phone;
	}
	
	// getter function for email
	public String getEmail() {
		return this.email;
	}
	
	// getter function for fname
	public String getFname() {
		return this.fname;
	}
	
	// getter function for lname
	public String getLname() {
		return this.lname;
	}

	
	
	public int addAmount(int b) {
		Database db = new Database();
		int i =  db.updateBalance( this, b, 1 );
		if( i == 1 ) {
			b += Integer.valueOf( this.balance );
			balance = String.valueOf(b);
		}
		return i;
	}
	
	public int removeAmount(int b) {
		if( b > Integer.valueOf(this.balance) ) {
			return 2;
		}
		else if(this.getType().equals("savings") && (Integer.valueOf(this.balance)-b)<500) {
			return 3;
		}
		Database db = new Database();
		int i =  db.updateBalance( this, b, 2 );
		if( i == 1 ) {
			b += Integer.valueOf( this.balance );
			balance = String.valueOf(b);
		}
		return i;
	}
	
//	public int removeAmountBill(int b, String phn) {
//		if( b > Integer.valueOf(this.balance) ) {
//			return 2;
//		}
//		Database db = new Database();
//		b *= -1;
//		int i =  db.updateBalanceBill( this, b, phn );
//		if( i == 1 ) {
//			b += Integer.valueOf( this.balance );
//			balance = String.valueOf(b);
//		}
//		return i;
//	}
	
	
	public void updateBalance() {
		Database db = new Database();
		String b = db.getBalance(this.account_number);
		if( b.compareTo("") == 0 ) {
		}
		else {
			this.balance = b;
		}
	}
	
	public int transferMoney( String to_acc, int amt) {
		Database db = new Database();
		return db.TransferMoney(this, to_acc, amt);
	}
	
	public int payBill( String conn_id, int amt) {
		Database db = new Database();
		return db.PayBill(this, conn_id, amt);
	}
	
	public List<Transactions> getTransactions( String acc_num) {
		Database db = new Database();
		List<Transactions> list = db.getTransactions( acc_num);
		return list;
	}
	
	public int changePassword(String pass1, String pass2) {
		if(pass1.equals("")  || pass2.equals("") ) {
			return -1;
		}
		else if(!pass1.equals(pass2)) {
			return -2;
		}
		else {
			Database db = new Database();
			db.change_password(pass1, this.account_number);
			return 1;
		}
		
	}
	
	
	
	
	public void finalize() {
        
	}
	
	
}
