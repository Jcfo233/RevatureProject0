package com.revature.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.instances.Account;
import com.revature.instances.User;
import com.revature.instances.UserType;
import com.revature.util.ConnectionUtil;
import com.revature.util.LoggerUtil;

public class UserDAOImpl implements UserDAO {

	public boolean insert(User user) {

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "{ call insert_into_user(?, ?, ?, ?, ?) }";
			
			CallableStatement stmt = conn.prepareCall(sql);
			
			stmt.setString(1, user.getFirstname());
			stmt.setString(2, user.getLastname());
			stmt.setString(3, user.getUsername());
			stmt.setString(4, user.getPassword());
			stmt.setString(5, user.getAccess().toString());
			
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in UserDaoImpl: insert");
			return false;
		}
	}

	public boolean update(User user) {
		
		try (Connection conn = ConnectionUtil.getConnection()){
		String sql = "UPDATE users SET first_name = ?, last_name = ?, username = ?"
				+ ", password = ? WHERE user_id = " + user.getId();
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1, user.getFirstname());
		stmt.setString(2, user.getLastname());
		stmt.setString(3, user.getUsername());
		stmt.setString(4, user.getPassword());
		
		stmt.execute();
		stmt.close();
		return true;
		}catch (SQLException e) {
			LoggerUtil.getLogger().info("SQL Exception in UserDAOImpl: update");
		}
		return false;
	}

	public List<User> findAll() {
		List<User> list = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM USERS";
			
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
					int id = rs.getInt("user_id");
					String firstname = rs.getString("first_name");
					String lastname = rs.getString("last_name");
					String username = rs.getString("username");
					String password = rs.getString("password");
					UserType access = UserType.valueOf(rs.getString("user_type"));
					
					User u = new User(id, firstname, lastname, username, password, access, new ArrayList<Integer>());
					u.setAccounts(new UserDAOImpl().findAccounts(id));
					
					list.add(u);
				}
			stmt.close();
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in UserDaoImpl");
		}
		
		return list;
	}

	public User findByUserPass(String user, String pass) {
		User u = null;

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM USERS WHERE username = ? AND password = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, user);
			stmt.setString(2, pass);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
					int id = rs.getInt("user_id");
					String firstname = rs.getString("first_name");
					String lastname = rs.getString("last_name");
					String username = rs.getString("username");
					String password = rs.getString("password");
					UserType access = UserType.valueOf(rs.getString("user_type"));
					
					u = new User(id, firstname, lastname, username, password, access, new ArrayList<Integer>());
					u.setAccounts(new UserDAOImpl().findAccounts(id));
					
					
				}
			stmt.close();
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in UserDaoImpl");
		}
		
		return u;
	}

	public List<User> findByName(String firstname, String lastname) {
		List<User> users = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM USERS WHERE first_name = ? AND last_name = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, firstname);
			stmt.setString(2, lastname);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
					int id = rs.getInt("user_id");
					String first = rs.getString("first_name");
					String last = rs.getString("last_name");
					String username = rs.getString("username");
					String password = rs.getString("password");
					UserType access = UserType.valueOf(rs.getString("user_type"));
					
					User u = new User(id, first, last, username, password, access, new ArrayList<Integer>());
					u.setAccounts(new UserDAOImpl().findAccounts(id));
					users.add(u);
					
				}
			stmt.close();
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in UserDaoImpl");
		}
		
		return users;
	}

	public List<User> findByLast(String lastname) {
		List<User> users = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM USERS WHERE last_name = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, lastname);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
					int id = rs.getInt("user_id");
					String first = rs.getString("first_name");
					String last = rs.getString("last_name");
					String username = rs.getString("username");
					String password = rs.getString("password");
					UserType access = UserType.valueOf(rs.getString("user_type"));
					
					User u = new User(id, first, last, username, password, access, new ArrayList<Integer>());
					u.setAccounts(new UserDAOImpl().findAccounts(id));
					users.add(u);
					
				}
			stmt.close();
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in UserDaoImpl");
		}
		
		return users;
	}

	public User findById(int id) {
		User u = new User();

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM USERS WHERE user_id = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
					int userid = rs.getInt("user_id");
					String first = rs.getString("first_name");
					String last = rs.getString("last_name");
					String username = rs.getString("username");
					String password = rs.getString("password");
					UserType access = UserType.valueOf(rs.getString("user_type"));
					
					u = new User(userid, first, last, username, password, access, new ArrayList<Integer>());
					u.setAccounts(new UserDAOImpl().findAccounts(id));
				}
			stmt.close();
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in UserDaoImpl");
		}
		return u;
	}

	public boolean remove(int id) {

		try (Connection conn = ConnectionUtil.getConnection()) {
			
			String sql = "{ call remove_user(?) }";
			
			CallableStatement stmt = conn.prepareCall(sql);
		
			stmt.setInt(1, id);
			
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in UserDaoImpl");
		}
		
		return false;
	}

	public List<User> findByAccount(Account a) {
		List<User> list = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT User_id FROM USER_ACCT_JT WHERE account_id = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, a.getId());
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				String sql2 = "SELECT * FROM Users WHERE user_id = ?";
				PreparedStatement stmt2 = conn.prepareStatement(sql);
				
				stmt2.setInt(1, rs.getInt("User_id"));
				
				ResultSet rs2 = stmt2.executeQuery();
				while(rs2.next()) {
				int id = rs2.getInt("user_id");
				String firstname = rs2.getString("first_name");
				String lastname = rs2.getString("last_name");
				String username = rs2.getString("username");
				String password = rs2.getString("password");
				UserType usertype = UserType.valueOf(rs2.getString("user_type"));
				
				User u = new User(id, firstname, lastname, username, password, usertype, new ArrayList<Integer>());
				ArrayList<Integer> accounts = new UserDAOImpl().findAccounts(id);
				u.setAccounts(accounts);
				list.add(u);
				}				
			}
			stmt.close();
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in UserDaoImpl");
		}
		
		return list;
	}
	
	public ArrayList<Integer> findAccounts(int id){
		ArrayList<Integer> list = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "Select account_id FROM USER_ACC_JT WHERE user_id = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				list.add(rs.getInt("account_id"));
			}
			stmt.close();
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL EXCEPTION in findAccounts, UserDAOImpl");
		}
		
		return list;
	}

	@Override
	public List<User> findByUser(String nextLine) {
		List<User> users = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM USERS WHERE username = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, nextLine);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
					int id = rs.getInt("user_id");
					String first = rs.getString("first_name");
					String last = rs.getString("last_name");
					String username = rs.getString("username");
					String password = rs.getString("password");
					UserType access = UserType.valueOf(rs.getString("user_type"));
					
					User u = new User(id, first, last, username, password, access, new ArrayList<Integer>());
					u.setAccounts(new UserDAOImpl().findAccounts(id));
					users.add(u);
					
				}
			stmt.close();
			
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in UserDaoImpl");
		}
		return users;
	}

}



