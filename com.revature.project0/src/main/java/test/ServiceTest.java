package test;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.instances.Account;
import com.revature.instances.User;
import com.revature.instances.UserType;
import com.revature.services.AccountService;
import com.revature.services.UserService;
import com.revature.instances.Application;
import com.revature.services.ApplicationService;
public class ServiceTest {
	private Account withdrawAccount = null;
	private Account depositAccount = null;
	private AccountService accserv = new AccountService();
	private User user = null;
	private Application app = null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		withdrawAccount = new Account(1, new ArrayList<Integer>(), 100);
		depositAccount = new Account(2, new ArrayList<Integer>(), 100);
		user = new User(1, "Administrator", "passwor", "username", "passwor", UserType.CUSTOMER, new ArrayList<Integer>());
		app = new Application(user, -1);
	}

	@After
	public void tearDown() throws Exception {
		UserService userserv = new UserService();
		ArrayList<Integer> accs = new ArrayList<Integer>();
		accs.add(1);
		userserv.update(new User(1, "Julie", "Fowlkes", "administrator", "secretpass", UserType.ADMIN, accs));
	}

	@Test
	public void test0() {
		assertFalse(accserv.withdraw(withdrawAccount.getId(), withdrawAccount.getBalance(), -101));
	}
	@Test
	public void test1() {
		assertFalse(accserv.withdraw(withdrawAccount.getId(), withdrawAccount.getBalance(), 0));
	}
	@Test
	public void test2() {
		assertFalse(accserv.deposit(depositAccount.getId(), 0));
	}
	@Test
	public void test3() {
		assertFalse(accserv.transfer(withdrawAccount, depositAccount, 0));
	}
	@Test
	public void test4() {
		assertFalse(accserv.transfer(withdrawAccount, depositAccount, 101));
	}
	@Test
	public void test5() {
		assertTrue(accserv.withdraw(withdrawAccount.getId(), withdrawAccount.getBalance(), -100));
	}
	@Test
	public void test6() {
		assertTrue(accserv.deposit(withdrawAccount.getId(), 100));
	}
	@Test
	public void test7() {
		assertTrue(accserv.transfer(withdrawAccount, depositAccount, 1));
	}
	@Test
	public void test8() {
		assertFalse(new UserService().update(user));
	}
	@Test
	public void test9() {
		user.setUsername("usernam");
		user.setPassword("password");
		assertFalse(new UserService().update(user));
	}

	@Test
	public void test11() {
		assertFalse(new ApplicationService().approve(app));
	}
	
	@Test
	public void test10() {
		assertTrue(new ApplicationService().applyFor(3,1));
	}
}
