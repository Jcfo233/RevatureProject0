package com.revature.repository;

import java.util.List;

import com.revature.instances.Account;

public interface AccountDAO{
	
	public List<Account> findAll();
	
	public Account findByID(int id);
	
	public boolean transfer(int fromacc, int toacc, int funds);
	
	public boolean cancel(int accId);
	
	public boolean hasId(int accnum);

	boolean withdrawDeposit(int id, double amt);
	
	public List<Integer> findUsers(int id);
}
