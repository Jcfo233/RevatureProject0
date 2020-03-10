package com.revature.services;

import java.util.List;

import com.revature.instances.Account;
import com.revature.repository.AccountDAO;
import com.revature.repository.AccountDAOImpl;


public class AccountService {
	AccountDAO repository = null;
	
	public AccountService() {
		super();
		this.repository = new AccountDAOImpl();
	}
	
	public AccountService(AccountDAO repository) {
		super();
		this.repository =  repository;
	}
	
	
	public List<Account> findAll() {
		return repository.findAll();
	}

	public boolean hasId(int accnum) {
		return repository.hasId(accnum);		
	}

	public Account findByID(int id) {
		return repository.findByID(id);
	}

	public boolean withdraw(int id, double balance, int amt) {
		if(amt >= 0) {
			System.out.println("Must withdraw a positive amount!");
			return false;
		}
		if(-amt > balance) {
			System.out.println("Can't withdraw more than balance!");
			return false;
			
		}
		return repository.withdrawDeposit(id, amt);	
	}
	public boolean deposit(int id, int amt) {
		return repository.withdrawDeposit(id, amt);
	}

	public boolean transfer(Account acc, Account toacc, int amt) {
		if(amt <= 0) {
			System.out.println("Must transfer a positive amount!");
			return false;
		}
		if(amt > acc.getBalance()) {
			System.out.println("Can't transfer more than balance!");
			return false;
		}
		return repository.transfer(acc.getId(), toacc.getId(), amt);
	}
	
	public boolean cancel(int id) {
		return repository.cancel(id);
	}

}	