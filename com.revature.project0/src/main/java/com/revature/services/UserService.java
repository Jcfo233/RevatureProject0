package com.revature.services;

import java.util.List;

import com.revature.instances.User;
import com.revature.repository.UserDAO;
import com.revature.repository.UserDAOImpl;

public class UserService {
	UserDAO repository = null;
	
	
	public UserService() {
		super();
		this.repository = new UserDAOImpl();
	}
	

	public UserService(UserDAO repository) {
		super();
		this.repository = repository;
	}


	public List<User> findByName(String nextLine) {
		return repository.findByLast(nextLine);
	}


	public boolean insert(User user) {
		return repository.insert(user);
	}


	public User findByUserPass(String username, String password) {
		return repository.findByUserPass(username, password);
	}


	public boolean remove(int id) {
		return repository.remove(id);
	}


	public List<User> findAll() {
		return repository.findAll();
	}


	public boolean update(User user) {
		if(user.getUsername().length() < 8 || user.getPassword().length() < 8) {
			System.out.println("Username and Password must be at least 8 characters long!");
			return false;
		}
		return repository.update(user);
		
	}

	public List<User> findByUser(String nextLine) {
		return repository.findByUser(nextLine);
	}
	
	public User findById(int id) {
		return repository.findById(id);
	}
}
