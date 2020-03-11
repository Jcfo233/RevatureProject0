package com.revature.services;

import java.util.List;

import com.revature.instances.Account;
import com.revature.instances.Application;
import com.revature.repository.ApplicationDAO;
import com.revature.repository.ApplicationDAOImpl;

public class ApplicationService{
	ApplicationDAO repository = null;
	
	
	public ApplicationService() {
		super();
		this.repository = new ApplicationDAOImpl();
	}
	

	public ApplicationService(ApplicationDAO repository) {
		super();
		this.repository = repository;
	}


	public boolean applyFor(int userid, int accID) {
	   if(new AccountService().findByID(accID) != null) {
		   return repository.applyFor(userid, accID);
	   }else {
		   System.out.println("Account does not exist!");
		   return false;
	   }
	}

	public boolean applyFor(int user) {
		return repository.applyFor(user);
	}


	public boolean approve(Application application) {
		if(new AccountService().findByID(application.getAccid()) != null || application.getAccid() == 0) {
			return repository.approve(application);
		}else{
			 System.out.println("Account Does not exist!");
			 return false;
		}
		
	}


	public List<Application> findAll() {
		return repository.findAll();
	}


	public boolean deny(Application application) {
		return repository.deny(application);
	}
}
