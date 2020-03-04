package com.revature.services;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.instances.Account;
import com.revature.instances.User;
import com.revature.instances.UserType;

public class Menu {
	private static Logger log = Logger.getLogger(Menu.class);

	private Menu() {
		super();
	}
	
	public static int getOption(Scanner s, int range) {
		boolean inRange = true;
		String option = s.nextLine();
		while (inRange) {
			try {
				Integer.parseInt(option);
				inRange = false;
				if (Integer.valueOf(option) >= range) {
					log.info("There are only " + range + " menu options \n Please enter the number of the menu option");
					option = s.nextLine();
					inRange = true;
				}
			}catch(NumberFormatException e) {
				log.info("Please enter the number of the menu option, e");
				option = s.nextLine();
			}
		}
		return Integer.valueOf(option);
	}
	public static User login(Scanner s) {
		User u = null;
		while(u == null) {
			log.info("Enter Username: ");
			String username = s.nextLine();
			log.info("Enter Password: ");
			String password = s.nextLine();
			u = findUser(username, password);
			if(u == null) {
				log.info("Could not find that account \n"
						+ "1: Try again? \n"
						+ "2: Back to login");
				int option = Menu.getOption(s, 2);
				switch(option) {
				case 1:
					break;
				case 2:
					return null;
				}
			}
		}
		return u;
	}
	public static User register(Scanner s) {
		String password;
		boolean badpass = false;
		log.info("Enter First Name: ");
		
		log.info("Enter Username for new Account: ");
		String username = s.nextLine();
		do {
		if (badpass) {
			log.info("Passwords didn't match!");
		}
		log.info("Enter Password for new Account: ");
		password = s.nextLine();
		log.info("Confirm Password: ");
		badpass = true;
		}while(!password.equals(s.nextLine())); 
			
		return new User();
		
	}
	public static void withdrawDeposit(Scanner s, UserType access) {
		if (access == UserType.ADMIN) {
		log.info("1: View accounts");
		log.info("2: Find an account");
		int option = Menu.getOption(s, 2);
		log.info("Enter Account Number, Username,\n or Account Owner Name: ");
			Account a = findAccount(s.nextLine());
		}
			
			
	}
}
