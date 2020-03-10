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
import com.revature.util.ConnectionUtil;
import com.revature.util.LoggerUtil;

public class AccountDAOImpl implements AccountDAO {

	
	public List<Account> findAll() {
		List<Account> list = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM accounts";
			
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("account_id");
				double balance = rs.getDouble("balance");
				
				Account a = new Account(id, new ArrayList<Integer>(), balance);
				
				List<Integer> users = new AccountDAOImpl().findUsers(a.getId());
				a.setUsers(users);
				
				list.add(a);
			}
			stmt.close();
		} catch (SQLException e) {
			LoggerUtil.log.error("SQLException AccountDAOImpl");
		}
		
		return list;
	}
	public List<Integer> findUsers(int id) {
		ArrayList<Integer> list = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "Select user_id FROM USER_ACC_JT WHERE account_id = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				list.add(rs.getInt("user_id"));
			}
			stmt.close();
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL EXCEPTION in findAccounts, UserDAOImpl");
		}
		return list;
	}
	public List<Account> findByName(String name) {
		
			List<Account> list = new ArrayList<>();
			try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT a.account_id, a.balance FROM "
						+ "Users "
						+ "INNER JOIN USER_ACCT_JT ON USERS.user_id = USER_ACCT_JT.user_id"
						+ "INNER JOIN ACCOUNTS ON USER_ACC_JT.account_id = ACCOUNTS.account_id"
						+ "WHERE u.last_name = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, name);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("account_id");
				double balance = rs.getDouble("balance");
				
				Account a = new Account(id, new ArrayList<Integer>(), balance);
				
				List<Integer> users = new AccountDAOImpl().findUsers(a.getId());
				a.setUsers(users);
				
				list.add(a);
				
			}
			stmt.close();
			}catch (SQLException e) {
				LoggerUtil.getLogger().info("SQLException in AccountDAOImpl: findByName");
			}
		return list;
	}

	public Account findByID(int id) {
		Account a = new Account();
		try (Connection conn = ConnectionUtil.getConnection()){
		String sql = "SELECT account_id, balance FROM ACCOUNTS WHERE account_id = " + id;
		
		Statement stmt = conn.createStatement();
	
		
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			int accid = rs.getInt("account_id");
			double balance = rs.getDouble("balance");
			
			a = new Account(accid, new ArrayList<Integer>(), balance);
			
			List<Integer> users = new AccountDAOImpl().findUsers(a.getId());
			a.setUsers(users);
			
			
		}
		stmt.close();
		}catch (SQLException e) {
			LoggerUtil.getLogger().info("SQLException in AccountDAOImpl: findByName");
		}
	return a;
	}

	public boolean transfer(int fromacc, int toacc, int funds) {
		try(Connection conn = ConnectionUtil.getConnection()) {
			
			String sql = "{ call transfer_funds(?, ?, ?) }";
			
			CallableStatement stmt = conn.prepareCall(sql);
			
			stmt.setInt(1, fromacc);
			stmt.setInt(2, toacc);
			stmt.setDouble(3, funds);
			
			stmt.execute();
			stmt.close();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	@Override
	public boolean cancel(int accId) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			
			String sql = "{ call remove_account(?) }";
			
			CallableStatement stmt = conn.prepareCall(sql);
		
			stmt.setInt(1, accId);
			
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in UserDaoImpl");
		}
		
		return false;
	}

	@Override
	public boolean withdrawDeposit(int id, double amt) {

		try (Connection conn = ConnectionUtil.getConnection()){
		String sql = "UPDATE accounts SET balance = (balance + " + amt + ") WHERE account_id = " + id;
		
		Statement stmt = conn.createStatement();
		
		stmt.execute(sql);
		stmt.close();
		return true;
		}catch (SQLException e) {
			LoggerUtil.getLogger().info("SQL Exception in UserDAOImpl: update");
		}
		return false;
	}

	@Override
	public boolean hasId(int accnum) {
		Account a = new Account();
		try (Connection conn = ConnectionUtil.getConnection()){
		String sql = "SELECT account_id, balance FROM ACCOUNTS WHERE account_id = " + accnum;
		
		Statement stmt = conn.createStatement();
	
		
		ResultSet rs = stmt.executeQuery(sql);
		return rs.next();
		}catch (SQLException e) {
			LoggerUtil.getLogger().info("SQLException in AccountDAOImpl: findByName");
		}
		return false;
	}

}
