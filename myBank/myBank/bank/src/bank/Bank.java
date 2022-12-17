package bank;

import javax.swing.*;

public class Bank {
	
	public static void main(String[] args) {
		JFrame frame=new JFrame("Assignment - Banking Operations");
		Front_End interf = new Front_End();
		Users user = new Users();
	    interf.showLogin(frame, user);
		frame.setSize(800,500);  
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}
