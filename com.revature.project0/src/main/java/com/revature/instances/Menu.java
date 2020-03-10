package com.revature.instances;

import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.services.AccountService;
import com.revature.services.ApplicationService;
import com.revature.services.BusinessLogic;
import com.revature.services.UserService;
import com.revature.util.LoggerUtil;
import com.revature.util.ScannerUtil;

public class Menu {
	public static final Logger log = Logger.getLogger(Menu.class);

	private Menu() {
		super();
	}
	
	public static User login() {
		Scanner s = ScannerUtil.getScanner();
		User u = null;
		UserService userserv = new UserService();
		while(u == null) {
			System.out.print("Enter Username: ");
			String username = s.nextLine();
			System.out.print("Enter Password: ");
			String password = s.nextLine();
			u = userserv.findByUserPass(username, password);
			if(u == null) {
				log.info("Could not find that account \n"
						+ "1: Try again? \n"
						+ "2: Back to login");
				int option = BusinessLogic.getOption(2);
				if(option == 2) {
					return null;
				}
			}
		}
		return u;
	}
	public static User register(User user) {
		Scanner s = ScannerUtil.getScanner();
		UserService userserv = new UserService();
		String password;
		boolean badpass = false;
		log.info("Enter First Name: ");
		user.setFirstname(s.nextLine());
		System.out.print("Enter Last Name: ");
		user.setLastname(s.nextLine());
		log.info("Enter Username for new Account: ");
		user.setUsername(s.nextLine());
		
		do {
		if (badpass) {
			log.info("Passwords didn't match!");
		}
		log.info("Enter Password for new Account: ");
		password = s.nextLine();
		log.info("Confirm Password: ");
		badpass = true;
		}while(!password.equals(s.nextLine()));
		user.setPassword(password);
		boolean success;
		if(user.getId() == 0) {
			user.setAccess(UserType.CUSTOMER);
			success = userserv.insert(user);
			if (success) {
				LoggerUtil.getLogger().info("User successfully registered");
			}
		}else {
			success = userserv.update(user);
			if(success) {
				LoggerUtil.getLogger().info("User successfully updated");
			}
		}
		if(success) {
			return userserv.findByUserPass(user.getUsername(), user.getPassword());
		}else {
			LoggerUtil.getLogger().info("User info registration failed!");
			return null;
		}
	}
	
	public static void mainMenu(User user) {
		boolean inMenu = true;
		while(inMenu) {
		System.out.print("--------------------------\n "
				+ "Welcome, " + user.getFirstname() + ", Thank you for choosing the Julie Banking Tool\n"
				+ "1: Manage Accounts \n"
				+ "2: " + ((user.getAccess() == UserType.CUSTOMER) ?  "Apply for ": "Approve ") + "Accounts\n"
				+ "3: " + ((user.getAccess() == UserType.CUSTOMER) ? "Manage Profile" : "Manage Users") + "\n"
				+ "4: Logout \n"						
				+ "--------------------------\n");
		int option = BusinessLogic.getOption(7);
		switch(option) {
		case 1:
			//If user is bank admin, finds any account then asks to withdraw or deposit
			//Otherwise, it allows the user to access the accounts tied to them
			Menu.accountMenu(user);
			break;
		case 2:
			Menu.applicationMenu(user);
			break;
		case 3:
			Menu.userMenu(user);
			break;
		case 4:
			inMenu = false;
			break;
		default:
			log.error("Unaccessible Switch Option in Main Menu!");
			}
		}
	}

	public static void accountMenu(User user) {
		boolean inMenu = true;
		int option;
		while(inMenu) {
			if (user.getAccess() != UserType.CUSTOMER) {
				System.out.println("1: View your accounts");
				System.out.println("2: Find an account");
				System.out.println("3: Back");
				
				option = BusinessLogic.getOption(3);
				
				switch(option) {
				
				case 1:
					inMenu = Menu.userAccounts(user);
					break;
				case 2:
					Menu.findAccounts(user);
					break;
					
					
				case 3:
					inMenu = false;
					break;
					
				default: LoggerUtil.log.info("Unaccessable option in accountMenu!");
				}	
			}else{inMenu = Menu.userAccounts(user);}
		}
			

	}
	public static boolean userAccounts(User user) {
		int menunum = 1;
		int option;
		AccountService accserv = new AccountService();
		while(true) {
			if (user.getAccess() == UserType.ADMIN) {
				System.out.println("Choose an account");
				for(int accountID: user.getAccounts()) {
				System.out.println(menunum++ + ": " + accserv.findByID(accountID));
				}
				System.out.println(menunum + ": Back");
				option = BusinessLogic.getOption(menunum);
				if(menunum == option) {
					return true;
				}
				Menu.transactionMenu(user.getAccounts().get(option-1), user);
			}else {
				for(int accountID: user.getAccounts()) {
					System.out.println(menunum++ + ": " + accountID);
					}
					if (menunum == 0) {
						System.out.println("No accounts found!");
					}
					System.out.println(menunum + ": Back");
					option = BusinessLogic.getOption(menunum);
					if(option != menunum) {
						Menu.transactionMenu(user.getAccounts().get(option-1), user);
					}
					return false;
			}
		}
			
	}
		
	public static void transactionMenu(int id, User user) {
		AccountService accserv = new AccountService();
		Account acc = accserv.findByID(id);
		int option;
		int amt;
		boolean inWithdraw = true;
		while(inWithdraw) {
			System.out.println(acc.toString());
			System.out.println("What would you like to do?");
			System.out.println("1: Withdraw");
			System.out.println("2: Deposit"
							+ "\n3: Transfer funds to another account"
							+ "\n4: View Account Details");
			System.out.println("5: Back");
			option = BusinessLogic.getOption(4);
		
			switch(option) {
		
			case 1: 
				System.out.println("How much would you like to withdraw?");
				amt = BusinessLogic.getInteger();
				if(accserv.withdraw(id, acc.getBalance(), -amt)) {
					LoggerUtil.getLogger().info("$" + amt + " successfully withdrawn from Account: " + acc.toString());
					acc.setBalance(acc.getBalance() - amt);
				}else {
					LoggerUtil.getLogger().info("Failed to withdraw $" + amt + "from Account: " + acc.toString());
				}
				
				break;
			case 2:
				System.out.println("How much would you like to deposit?");
				amt = BusinessLogic.getInteger();
				if(accserv.deposit(id, amt)) {
					LoggerUtil.getLogger().info("$" + amt + " successfully deposited to Account: " + acc.toString());
					acc.setBalance(acc.getBalance() + amt);
				}else {
					LoggerUtil.getLogger().info("Failed to deposit $" + amt + "to Account: " + acc.toString());
				}
				break;
			case 3:
				System.out.println("Which account would you like to transfer to?");
				Account toacc = accserv.findByID(BusinessLogic.getInteger());
				if (toacc == null) {
					System.out.println("Account not found!");
					break;
				}
				System.out.println("How much would you like to transfer?");
				amt = BusinessLogic.getInteger();
				if(accserv.transfer(acc, toacc, amt)) {
					LoggerUtil.getLogger().info("$" + amt + " successfully transferred from " + acc.toString() + " to " + toacc.toString());
					acc.setBalance(acc.getBalance() - amt);
				}else {
					LoggerUtil.getLogger().info("Failed to transfer $" + amt + "from " + acc.toString() + " to " + toacc.toString());
				}
				break;
			case 4:
				System.out.println(acc.toString());
				if (user.getAccess() == UserType.ADMIN) {
					System.out.println("What would you like to do?"
							+ "\n1: Cancel Account"
							+ "\n2: Back");
					if(BusinessLogic.getOption(2) == 1) {
						if(accserv.cancel(acc.getId())) {
							LoggerUtil.getLogger().info("Successfully cancelled " + acc.toString());
						}else {
							LoggerUtil.getLogger().info("Failed to cancel " + acc.toString());
						}
					}
				}
			case 5:
				inWithdraw = false;
				break;
			default:
				LoggerUtil.log.info("Invalid menu option in transaction menu!");
			}
		}
	}
	
	public static void findAccounts(User user) {
		int accChoice = 1;
		List<Account> foundAccount = new ArrayList<>();
		System.out.println("Account(s) Found:");
		for(Account a: Menu.findAccount()){
			System.out.println(accChoice++ + ": " + a.toString());
			foundAccount.add(a);
		}
		System.out.println(accChoice++ + ": Different query");
			
			System.out.println(accChoice + ": Back");
			if(user.getAccess() == UserType.ADMIN) {
			int option = BusinessLogic.getOption(accChoice);
			if(option == accChoice - 1) {
				Menu.findAccounts(user);
				
			}else if (option != accChoice){
				Menu.transactionMenu(foundAccount.get(option -1).getId(), user);
			}
		}
	}
	
	public static List<Account> findAccount(){
		AccountService accserv = new AccountService();
		Scanner s = ScannerUtil.getScanner();
		Logger log = Logger.getLogger(AccountService.class);
		List<Account> a = new ArrayList<>();
		System.out.println("1: Find by account ID "
				+ "\n2: Find All "
				+ "\n3: Back");
		int option = BusinessLogic.getOption(5);
		switch(option) {
		case 1:
			System.out.print("Enter Account ID:");
			a.add(accserv.findByID(BusinessLogic.getInteger()));			
			return a;	
		case 2:
			return accserv.findAll();
		case 3:
			return new ArrayList<Account>();
		default:
			log.info("Invalid option in AccountService.findAccount");
			return new ArrayList<Account>();
		}
	}
	

	public static void applicationMenu(User user) {
		AccountService accserv = new AccountService();
		ApplicationService appserv = new ApplicationService();
		if(user.getAccess() == UserType.CUSTOMER) {
			System.out.println("Are you applying for an existing account?");
			System.out.println("1: Yes");
			System.out.println("2: No");
			if(1 == BusinessLogic.getOption(2)) {
				System.out.println("What is the account number of this account?");
				Account accID = accserv.findByID(BusinessLogic.getInteger());
				if(appserv.applyFor(user.getId(), accID)) {
					LoggerUtil.getLogger().info("Account Successfully Applied For!");
				}else {
					LoggerUtil.getLogger().info("Failed To Apply For Account!");
				}
				
				
			}else {
				if(appserv.applyFor(user.getId())) {
					LoggerUtil.getLogger().info("Account Successfully Applied For!");
				}else {
					LoggerUtil.getLogger().info("Failed To Apply For Account!");
				}
			}
		}else {
			Menu.approveApplication();
		}
		
	}
	
	public static void approveApplication() {
		ApplicationService appserv = new ApplicationService();
		while(true) {
		List<Application> openapps = appserv.findAll();
			int menuNum = 1;
			System.out.println("Which application would you like to view?");
			for(Application a : openapps) {
				System.out.println(menuNum++ + ": "  + a.toString());
			}
			System.out.println(menuNum + ": Back");
			int option = BusinessLogic.getOption(menuNum);
			if(menuNum == option) {
				return;
			}
			System.out.println("What would you like to do?");
			System.out.println("1: Approve\n"
					+ "2: Deny\n"
					+ "3: Cancel\n");
			int approve = BusinessLogic.getOption(3);
			switch (approve) {
			case 1:
				if (appserv.approve(openapps.get(option - 1))) {
					LoggerUtil.getLogger().info("Application Approved!");
				}else {
					LoggerUtil.getLogger().info("Failed To Approve Application");
				}
				break;
			case 2:
				if (appserv.deny(openapps.get(option-1))) {
					LoggerUtil.getLogger().info("Application has been denied!");
				}else {
					LoggerUtil.getLogger().info("Failed To Deny Application!");
				}
				break;
			case 3:
				break;
			default:
				LoggerUtil.log.info("Impossible option at ApproveApplication!");
			}
		}
	}
	
	private static void userMenu(User user) {
		UserService userserv = new UserService();
		boolean inMenu = true;
		while(inMenu) {
		if (user.getAccess() == UserType.CUSTOMER) {
			System.out.println("What would you like to do?\n"
					+ "1: Change Username\n"
					+ "2: Change Password\n"
					+ "3: Back");
			int option = BusinessLogic.getOption(3);
			if( option == 1) {
				System.out.print("Enter new username:");
				user.setUsername(ScannerUtil.s.nextLine());
				if (userserv.update(user)) {
					LoggerUtil.getLogger().info("Username Successfully Changed!");
				}else {
					LoggerUtil.getLogger().info("Failed to update Username!");
				}
			}else if (option == 2){
				System.out.print("Enter new password:");
				String newpass = ScannerUtil.s.nextLine();
				System.out.print("Confirm new password:");
				if(newpass.equals(ScannerUtil.s.nextLine())) {
					user.setPassword(newpass);
					if(userserv.update(user)) {
						LoggerUtil.getLogger().info("Password Successfully Updated!");
					}
				}
				else {
					System.out.println("Passwords didn't match!");
				}
			}else {
				inMenu = false;
			}
			
		}else if (UserType.EMPLOYEE == user.getAccess()) {
			Menu.manageUserMenu(user);
		}else {
			System.out.println("What would you like to do?"
					+ "\n1: Edit Users"
					+ "\n2: Create New Users"
					+ "\n3: Back");
			int option = BusinessLogic.getOption(3);
			if (option == 1) {
				Menu.manageUserMenu(user);
			}else if (option == 2) {
				Menu.createUserMenu();
			}else {
				inMenu = false;
			}
		}
		}
	}

	private static void createUserMenu() {
		Scanner s = ScannerUtil.getScanner();
		UserService userserv = new UserService();
		String password;
		boolean badpass = false;
		System.out.print("Enter First Name: ");
		String firstname = s.nextLine();
		System.out.print("Enter Last Name: ");
		String lastname = s.nextLine();
		System.out.print("Enter Username for new Account: ");
		String username = s.nextLine();
		
		do {
		if (badpass) {
			System.out.print("Passwords didn't match!");
		}
		System.out.print("Enter Password for new Account: ");
		password = s.nextLine();
		System.out.print("Confirm Password: ");
		badpass = true;
		}while(!password.equals(s.nextLine()));
		System.out.println("What type of account is this?\n"
				+ "1: Employee\n"
				+ "2: Admin");
		int option = BusinessLogic.getOption(2);
		User newuser = new User(0, firstname, lastname, username, password, ((option == 1) ? UserType.EMPLOYEE: UserType.ADMIN), new ArrayList<Integer>());
		if (userserv.insert(newuser)) {
			LoggerUtil.getLogger().info("User Successfully craeted!");
		}else {
			LoggerUtil.getLogger().info("Failed to Create User!");
		}
	}

	private static void manageUserMenu(User user) {
		UserService userserv = new UserService();
		List<User> users = new ArrayList<>();
		while(true) {
			System.out.println("How would you like to find a user?\n"
					+ "1: Find by user id\n"
					+ "2: Find by username"
					+ "3: Find by lastname"
					+ "4: Find all\n"
					+ "5: Back");
			int option = BusinessLogic.getOption(5);
			switch(option) {
			case 1:
				System.out.print("Enter Account Id: ");
				users.add(userserv.findById(BusinessLogic.getInteger()));
				break;
			case 2:
				System.out.print("Enter Username: ");
				users = userserv.findByUser(ScannerUtil.getScanner().nextLine());
			case 3:
				System.out.println("Enter User Last name: ");
				users = userserv.findByName(ScannerUtil.getScanner().nextLine());
			case 4:
				users = userserv.findAll();
				break;
			case 5:
				return;
			default:
				LoggerUtil.log.info("Impossible option in manageUserMenu");
				return;
			}
			int menuNum = 1;
			if (UserType.ADMIN == user.getAccess()) {
				System.out.println("Which User would you like to access?");
			}
			for(User u: users) {
					System.out.println(menuNum++ + ": " + u.toString());
			}
			System.out.println(menuNum + ": Back");
			option = BusinessLogic.getOption(menuNum);
			if(option != menuNum && UserType.ADMIN == user.getAccess()) {
					System.out.println("What would you like to do?\n"
							+ "1: Edit User Information\n"
							+ "2: Back");
					if (1 == BusinessLogic.getOption(2)) {
						System.out.println("What would you like to do?\n"
								+ "1: Change User info\n"
								+ "2: Remove User\n"
								+ "3: Back");
						int option2 = BusinessLogic.getOption(3);
						if( option2 == 1) {
							Menu.register(users.get(option-1));
						}else if (option2 == 2) {
							if(userserv.remove(users.get(option - 1).getId())) {
								LoggerUtil.getLogger().info(users.get(option - 1).toString() + "Successfully removed!");	
							}else {
								LoggerUtil.getLogger().info("Failed to remove " + users.get(option -1).toString());
							}
						}
					}
					
				}
		}
			
	
	}
}