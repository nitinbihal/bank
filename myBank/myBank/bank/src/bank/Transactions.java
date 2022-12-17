package bank;

public class Transactions {
	private String serial_no;
	private String amount;
	private String type;
	private String date;
	private String time;
	private String ref_no;
	
	
	// default constructor
	public Transactions() {
		this.serial_no = "";
		this.amount = "";
		this.type = "";
		this.date = "";
		this.time = "";
		this.ref_no = "";
		
		
	}
	
	// parameterized constructor
	public Transactions(String serial_no, String amount, String type, String date, String time, String ref_no) {
		this.serial_no = serial_no;
		this.amount = amount;
		this.type = type;
		this.date = date;
		this.time = time;
		this.ref_no = ref_no;

	}
	
	public String getSerialNo() {
		return this.serial_no;
	}
	
	public String getAmount() {
		return this.amount;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getTime() {
		return this.time;
	}
	
	public String getRefNo() {
		return this.ref_no;
	}
	


}
