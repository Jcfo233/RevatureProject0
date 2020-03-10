package com.revature.repository;

import java.util.List;

import com.revature.instances.User;

public interface UserDAO {
	
	public boolean insert(User user);
	
	public boolean update(User user);
	
	public List<User> findAll();
	
	public User findByUserPass(String user, String pass);
	
	public List<User> findByName(String firstname, String lastname);
	
	public List<User> findByLast(String lastname);
	
	public User findById(int id);
	
	public boolean remove(int id);
	
	public List<Integer> findAccounts(int id);

	public List<User> findByUser(String nextLine);
		
}
