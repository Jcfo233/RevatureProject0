package com.revature.repository;

import java.util.List;

import com.revature.instances.Application;

public interface ApplicationDAO {
	
	public List<Application> findAll();
	
	public boolean approve(Application application);

	public boolean applyFor(int userid);

	public boolean applyFor(int userid, int accid);
	
	public boolean deny(Application application);
}
