package com.revature.util;

import java.util.Scanner;

 public class ScannerUtil {
	public static Scanner s = new Scanner(System.in);
	
	private ScannerUtil() {
		super();
	}
	
	public static Scanner getScanner() {
		return s;
	}
	
	public static void closeScanner() {
		s.close();
	}
}
