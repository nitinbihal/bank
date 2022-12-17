package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;




import java.util.*;


public class Database {
	
	private String url = "jdbc:mysql://localhost:3306/bank";       
	private String username = "root";
	private String password = "";
	private Connection cnx;
	
	
	
	public Database() {    
        try {
            cnx = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established successfully!");
        }
        catch (SQLException e) {
            throw new IllegalStateException("Unable to connect to the database. " + e.getMessage());
        } 
	}
	
	
	
	public Users logIn( String uname, String passwd ) {
		Users user = new Users();
		try {	

			String sql = "Select * From bank.users Where uname = \""+uname+"\" and "
					+ "passwd = \""+passwd+"\"";
			System.out.println(sql);
			Statement l_st = cnx.createStatement();
			ResultSet l_result = l_st.executeQuery(sql);
			if( l_result.next() ) {
				user = new Users( l_result.getString("user_id"), l_result.getString("uname"), "");
	        }
		}
		catch (SQLException e) {
			System.out.println("SQL error");
		}
		return user;
	}
	
	
	public Account getAccount( String user_id ) {
		Account account = new Account();
		try {	
			String sql = "Select * from bank.accounts where account_number = "+user_id;
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			if( result.next() ) {
				account = new Account( result.getString("account_number"), result.getString("fname"), result.getString("lname"), result.getString("type"), result.getString("balance"), result.getString("status"), result.getString("phone"), result.getString("email"));
	        }
		}
		catch (SQLException e) {
			System.out.println("SQL Error " + e.getMessage());
		}
		return account;
	}
	

	public void reduce_balance(int amount, int account_number)
	{
		try
		{
			String sql = "Update bank.accounts set balance = " + amount + " where account_number = " + account_number;			
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			System.out.println("SQL Error");
		}
	}
	
	
	
	
	public int get_account_status(int account_number)
	{
		try {	
			String sql = "Select status From bank.accounts Where account_number = " + account_number;
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
	        if( result.next() ) {
	        		return result.getInt("status");
	        }
	        else 
	        	return -1;
		}
		catch (SQLException e) {
			System.out.println("SQL Error");
		}
		return -1;
	}
	

	
	public void close_account(int account_number)
	{
		try
		{
			String sql = "Update bank.accounts set status = 0 Where account_number = "+ account_number;
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			System.out.println("SQL Error");
		}
	}
	
	
	
	
	
	
	public int create_login(String uname, String passwd)
	{
		try
		{
			String sql = "INSERT INTO bank.users VALUES (NULL,\"" + uname + "\",\"" + passwd + "\");"; 
//			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			throw new IllegalStateException("SQL Error " + e.getMessage());
		}
		
		try
		{
			String sql = "select user_id from bank.users where username = \"" + username + "\" and password = \"" + password + "\"";
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
	        if( result.next() )
	        {
	        	return result.getInt("user_id");
	        }
	        else
	        	return -1;
		}
		catch (SQLException e)
		{
			System.out.println("SQL Error");
		}
		return -1;
	
	}
	
	

	//Create Account 
	public int createAccount( Account new_account ) {
		int res = 0;
		try {
			// Check if account already exists
			String sql = "Select * From bank.accounts Where phone = \""+new_account.getPhone()+"\"";
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
	        int row = 0;
	        
	        while( result.next() )
	        	row++;
	        
			if( row < 1 ) {
				
				
				// Insert new account
				String sql1 = "Insert Into bank.accounts Values(NULL, \""+new_account.getFname()+"\", \""+
						new_account.getLname()+"\", \""+
						new_account.getType()+"\", "+new_account.getBalance()+", -1, \""+
						new_account.getPhone()+"\", \""+new_account.getEmail()+"\")";
				System.out.println(sql1);
				Statement stmt1 = cnx.createStatement();
		        stmt1.executeUpdate(sql1);
		        
		        res = 1;
		        
		        
		        
		        //String sql2 = 'Insert into bank.users Values()'
		        
		         
			}
			else 
				res = 2;
		}
		catch (SQLException e) {
			System.out.println("SQL Error");
		}
		return res;
	}

	//Create User 
	public String createUser(String fname, String id ) {
		String uname = "";
		try {
			// Insert new User
			//System.out.println("Here");
			uname = fname.toLowerCase() + id;
			int user_id = Integer.parseInt(id);
			String sql = "Insert into bank.users Values("+user_id+", \""+uname+"\","+"\"1234\")";
			System.out.println(sql);
			Statement stmt1 = cnx.createStatement();
	        stmt1.executeUpdate(sql);
	       
		}
		catch (SQLException e) {
			System.out.println("SQL Error");
		}
		return uname;
	}
	
	
	
	public int TransferMoney( Account account, String toAccountNumber, int amount) {
		try {	
			
			String sql = "Select account_number, balance, type From bank.accounts Where account_number = "+toAccountNumber+" and status in (1,-1)";
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			
	        if( result.next() ) {
	        	
	        	int recv_balance = result.getInt("balance");
	        	int recv_user_id = result.getInt("account_number");
	        	
	        	String type = account.getType();
	        	int snd_balance = Integer.parseInt(account.getBalance());
				if( snd_balance >= amount ) {
					
					System.out.println(type);
					System.out.println(snd_balance-amount);
					if(type.equals("savings") && (snd_balance-amount)<500) {
						return 4;
					}
					snd_balance -= amount;
					recv_balance += amount;
					
					String sql1 = "Update bank.accounts Set balance = "+snd_balance+" where account_number=" +account.getAccountNum();		
					System.out.println(sql1);
					Statement stmt1 = cnx.createStatement();
			        stmt1.executeUpdate(sql1);
			        
			        String sql2 = "Update bank.accounts Set balance = "+recv_balance+" where account_number="+toAccountNumber;	
//					System.out.println(sql2);
					Statement stmt2 = cnx.createStatement();
			        stmt2.executeUpdate(sql2);
			        
			        String tsql1 = "Insert into bank.transactions values(NULL,"+account.getUserId()+"," + String.valueOf(amount)+", \"Debit\", CURDATE(), CURRENT_TIME(), "+toAccountNumber+" )";		
					System.out.println("SQL-> "+tsql1);
					Statement tstmt1 = cnx.createStatement();
			        tstmt1.executeUpdate(tsql1);
			        
			        String tsql2 = "Insert into bank.transactions values(NULL,"+recv_user_id+"," + String.valueOf(amount)+", \"Credit\", CURDATE(), CURRENT_TIME(), "+account.getAccountNum()+" )";		
					System.out.println("SQL-> "+tsql2);
					Statement tstmt2 = cnx.createStatement();
			        tstmt2.executeUpdate(tsql2);
			        
			        return 3;
				}
				else
					return 2;
	        }
	        else
	        	return 1;
		}
		catch (SQLException e) {
			System.out.println("SQL Error");
		}
		
		return 0;
	}
	
	
	public int PayBill( Account account, String conn_id, int amount) {
		try {
        	String type = account.getType();
        	int snd_balance = Integer.parseInt(account.getBalance());
			if( snd_balance >= amount ) {
				
				System.out.println(type);
				System.out.println(snd_balance-amount);
				if(type.equals("savings") && (snd_balance-amount)<500) {
					return 4;
				}
				snd_balance -= amount;
				
				
				String sql1 = "Update bank.accounts Set balance = "+snd_balance+" where account_number=" +account.getAccountNum();		
				System.out.println(sql1);
				Statement stmt1 = cnx.createStatement();
		        stmt1.executeUpdate(sql1);
		        
		        String remarks = "Bill : "+conn_id;
		        String tsql1 = "Insert into bank.transactions values(NULL,"+account.getUserId()+"," + String.valueOf(amount)+", \"Debit\", CURDATE(), CURRENT_TIME(), "+"\""+remarks+"\" )";		
				System.out.println("SQL-> "+tsql1);
				Statement tstmt1 = cnx.createStatement();
		        tstmt1.executeUpdate(tsql1);
		        return 3;
			}
			else
				return 2;
		}
		catch (SQLException e) {
			System.out.println("SQL Error");
		}
		
		return 0;
	}
	

	Account searchAccount2( String accountNum ) {
		Account account = new Account();
		try {	
			String sql = "Select * From bank.accounts Where account_number = "+accountNum;
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
	        
			if( result.next() ) {
				account = new Account( result.getString("account_number"), result.getString("fname"), result.getString("lname"), result.getString("type"), result.getString("balance"), result.getString("status"), result.getString("phone"), result.getString("email") );
	        }
	        return account;
		}
		catch (SQLException e) {
			System.out.println("SQL Error");
			return account;
		}
	}
	
	String searchAccount3( String phn ) {
		String acc_num = "";
		try {	
			String sql = "Select * From bank.accounts Where phone = "+phn;
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
	        
			if( result.next() ) {
				acc_num = result.getString("account_number");
	        }
	        return acc_num;
		}
		catch (SQLException e) {
			System.out.println("SQL Error");
			return acc_num;
		}
	}
	
	
	public List<Transactions> getTransactions(String accNum) {
		List<Transactions> list=new ArrayList<Transactions>();  
		try {	
			// finding the transactions
			String sql = "Select * From bank.transactions Where user_id = "+accNum+" order by date desc LIMIT 10";
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			while( result.next() ) {
				Transactions th = new Transactions( result.getString("t_id"), result.getString("amount"), result.getString("type"), result.getString("date"), result.getString("time"), result.getString("ref_no") );
				//System.out.print("@-");
				list.add( th );
	        }
		}
		catch (SQLException e) {
			System.out.println("Something went wrong");
		}
		return list;
	}

	

	
	public int updateBalance( Account account, int amount, int t ) {
		int res = 0;
		try {	
			// update balance of account
			int temp_balance = Integer.valueOf( account.getBalance() ) + amount;
			String sql = "Update bank.accounts Set balance = "+temp_balance+" where account_number="+account.getAccountNum();		
			
			Statement stmt = cnx.createStatement();
	        stmt.executeUpdate(sql);
	        
	        // check if it is updated successfully
        	String sql1 = "Select balance From bank.accounts Where account_number = "+account.getAccountNum();
			System.out.println(sql1);
			Statement stmt1 = cnx.createStatement();
			ResultSet result1 = stmt1.executeQuery(sql1);
			if( result1.next() ) {
				int b = result1.getInt("balance");
				if( b == temp_balance ) {
					
					String type_ = "Credit";
					String sql2 = "Insert into bank.transactions values(NULL,"+account.getUserId()+","+amount+", \""+type_+"\", CURDATE(), CURRENT_TIME()," + "\"Cash Deposit\" )";		
					
					if( t == 2 ) {
						type_ = "Debit";
						sql2 = "Insert into bank.transactions values(NULL,"+account.getUserId()+","+amount+", \""+type_+"\", CURDATE(), CURRENT_TIME()," + "\"Cash Withdrawal\" )";				
						}
	
					System.out.println(sql2);
					cnx.createStatement();
			        stmt.executeUpdate(sql2);
		
					res = 1;
				}
			}
	        return res;
		}
		catch (SQLException e) {
			System.out.println("SQL Error");
			return res;
		}	
	}
	
	

	

	
	
	
	
	
	
	
	public void updateAccountInfo(String account_number, String phone, String email ) {
	 try {   
		 	String sql = "Update bank.accounts Set phone = \""+phone+"\" , email = \""+email+"\"  where account_number="+account_number;		
			Statement stmt = cnx.createStatement();
	        stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
        	System.out.println("SQL Error");
        }
	}
	

	
	public String get_password(int user_id)
	{
		try {	
			String sql = "Select * From bank.users Where user_id = " + user_id;
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
	        if( result.next() ) {
	        		return result.getString("passwd");
	        }
	        else 
	        	return "";
		}
		catch (SQLException e) {
			System.out.println("SQL Error");
		}
		return "";
	}
	
	public void change_password(String passwd, String user_id)
	{
		try {	
			String sql = "Update bank.users set passwd = \"" + passwd + "\" Where user_id = " + user_id;
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			stmt.executeUpdate(sql);
			String sql2 = "Update bank.accounts set status = 1 Where user_id = " + user_id;
			stmt.executeUpdate(sql2);

		}
		catch (SQLException e) {
			System.out.println("SQL Error");
		}
	}
	
	public String getName(String user_id) {
		String name = "";
		try
		{
			String sql = "Select * From bank.employee Where user_id = "+ user_id;
			System.out.println(sql);
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
	        if( result.next() )
	        {
	        	name = result.getString("fname") + " " + result.getString("lname");
	        }
		}
		catch (SQLException e)
		{
			System.out.println("SQL Error");
		}
		return name;
	}
	
	public String getBalance(String account_number) {
		String b = "";
		try {	
			String sql = "Select * From bank.accounts Where account_number = "+account_number;
			Statement stmt = cnx.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			if( result.next() ) {
	        	 b = result.getString("balance");
	        }
		}
		catch (SQLException e) {
			System.out.println("SQL Error");
		}
		return b;
	}
	

	
	public void finalize() {
        try {   
        	System.out.println("Connection Closed");
            cnx.close();
        }
        catch (SQLException e) {
            throw new IllegalStateException("Error : " + e.getMessage());
        }
	}
	
}

