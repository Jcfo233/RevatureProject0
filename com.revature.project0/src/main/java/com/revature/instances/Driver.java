package com.revature.instances;

import org.apache.log4j.Logger;


import com.revature.services.BusinessLogic;

import java.util.Scanner;

import com.revature.util.LoggerUtil;
import com.revature.util.ScannerUtil;
public class Driver {
	
	private static Logger log = LoggerUtil.getLogger();
	public static final Scanner s = ScannerUtil.getScanner();
	public static void main(String args[]) {
		boolean inMenu = true;
		while (inMenu) {
				User user = new User();
				log.info("Launching banking tool");
				System.out.println( "Welcome to my Banking Tool \n"
					+ "Enter the number of the menu option \n"
					+ "1: Login \n"
					+ "2: Register \n"
					+ "3: Exit");
					
				int option = BusinessLogic.getOption(3);
			 
				switch(option) {
				
				case 1:
					user = Menu.login();
					if(user != null) {
						Menu.mainMenu(user);
					}
					break;
				case 2:
					user = Menu.register(user);
					if(user != null) {
						Menu.mainMenu(user);
					}
					break;
				default:
					inMenu = false;
				}
			
			
				
		}
		ScannerUtil.closeScanner();
	}
	
}