package com.revature.services;

import java.util.Scanner;

import com.revature.instances.Menu;
import com.revature.util.ScannerUtil;

public class BusinessLogic {

	public static int getOption(int range) {
		Scanner s = ScannerUtil.getScanner();
		boolean inRange = true;
		String option = s.nextLine();
		while (inRange) {
			try {
				Integer.parseInt(option);
				inRange = false;
				if (Integer.valueOf(option) > range || Integer.valueOf(option) <= 0) {
					Menu.log.info("There are only " + range + " menu options \n Please enter the number of the menu option:");
					option = s.nextLine();
					inRange = true;
				}
			}catch(NumberFormatException e) {
				Menu.log.info("Please enter the number of the menu option, e");
				option = s.nextLine();
			}
		}
		return Integer.valueOf(option);
	}

	public static int getInteger() {
		Scanner s = ScannerUtil.getScanner();
		boolean inRange = true;
		String option = s.nextLine();
		while (inRange) {
			try {
				Integer.parseInt(option);
				if(Integer.valueOf(option) <= 0)
				{
					System.out.println("Can't choose a negative number!");
					option = s.nextLine();
				}else {
				inRange = false;
				}
				}
			catch(NumberFormatException e) {
				Menu.log.info("Please enter a valid account Id");
				option = s.nextLine();
			}
		}
		return Integer.valueOf(option);
	}

}
