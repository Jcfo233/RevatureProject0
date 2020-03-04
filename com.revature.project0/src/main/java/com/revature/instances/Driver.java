package com.revature.instances;

import org.apache.log4j.Logger;

import com.revature.services.Menu;

import java.util.Scanner;

public class Driver {
	
	private static Logger log = Logger.getLogger(Driver.class);
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		boolean flag = true;
		User user = new User();
		while (flag) {
			boolean inLogin = true;
			while(inLogin) {
			log.info("-------------------------- \n"
					+ "Welcome to the Fortnite Banking Tool \n"
					+ "Enter the number of the menu option \n"
					+ "1: Login \n"
					+ "2: Register \n"
					+ "3: Exit");
					
			int option = Menu.getOption(scan, 3);
			 
			switch(option) {
				
			case 1:
				user = Menu.login(scan);
				if(user != null) {
					inLogin = false;
				}
				break;
			case 2:
				user = Menu.register(scan);
				if(user != null) {
					inLogin = false;
				}
				break;
			default:
				flag = false;
			}
			while(flag) {
				System.out.print("--------------------------\n "
						+ "Welcome, " + user.getFirstname() + ", Thank you for choosing the Fortnite Banking Tool"
						+ "1: Withdraw/Deposit \n" 
						+ "2: Transfer funds \n"
						+ "3: Account Registration \n"
						+ "4: Applications"
						+ "5: View Profile \n"
						+ "6: Logout \n"
						+ "7: Exit \n"
						+ "--------------------------");
				option = Menu.getOption(scan, 7);
				
				switch(option) {
				case 1:
					System.out.print("Enter the account number:");
					//If user is bank admin, finds any account then asks to withdraw or deposit
					//Otherwise, it allows the user to access the accounts tied to them
					Menu.withdrawDeposit(scan, user.getAccess());
				case 2:
					
				case 3:
				
				case 4:
				
				case 5:
				
				case 6:
				
				default:
				}
				
			}
			}
			
			
		}
		
		scan.close();
	}
	
}