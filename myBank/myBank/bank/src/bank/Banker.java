package bank;



public class Banker {
	
	private String name;
	
	Banker() {
		name = "Bank Clerk";
	}
	
	Banker(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	// other functions here
	public int createAccount(Account newAccount) {
		Database db = new Database();
		return db.createAccount(newAccount );
	}
	
	public String createUser(String fname, String id) {
		Database db = new Database();
		return db.createUser(fname, id);
	}
	

	
	
	int close_account(String account_num)
	{
		Database db = new Database();	
		int temp_status = db.get_account_status(Integer.parseInt(account_num));
		
		if ( temp_status == 1 || temp_status == -1)
		{
			db.close_account(Integer.parseInt(account_num));
			return 0;
		}
		else
			return -1;
	}
	
	public Account getAccountInfo(String account_number) {
		Database db = new Database();	
		Account account = db.searchAccount2(account_number);
		return account;
	}

	public String getAccountNum(String phone) {
		Database db = new Database();	
		String acc_num = db.searchAccount3(phone);
		return acc_num;
	}
	
	

	
}
