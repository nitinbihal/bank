package bank;

public class Users {
	private String user_id;
	private String uname;
	private String passwd;
	

	public Users() {
		this.user_id = "";
		this.uname = "";
		this.setPassword("");
		
	}
	
	public Users(String user_id, String username, String password) {
		this.user_id = user_id;
		this.uname = username;
		this.setPassword(password);
		
	}	
	
	public String getUserId() {
		return this.user_id;
	}
	
	public String getUsername() {
		return this.uname;
	}
	
	public boolean isBanker() {
		if( uname.compareTo("banker") == 0 )
			return true;
		else
			return false;
	}
	
//	public String getType() {
//		String type_ = "";
//		if( uname.compareTo("banker") == 0 )
//			type_ = "Banker";
//		else
//			type_ = "Client";
//		return type_;
//	}
	

	
	public String getName() {
		Database db = new Database();
		return db.getName( this.user_id );
	}

	public String getPassword() {
		return passwd;
	}
	
	public int changeDefaultPassword(String pass1, String pass2) {
		if(pass1.equals("")  || pass2.equals("") ) {
			return -1;
		}
		else if(!pass1.equals(pass2)) {
			return -2;
		}
		else {
			Database db = new Database();
			db.change_password(pass1, this.user_id);
			return 1;
		}
		
	}

	public void setPassword(String password) {
		this.passwd = password;
	}
	
}
