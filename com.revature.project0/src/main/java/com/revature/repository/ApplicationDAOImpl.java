package com.revature.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.instances.Account;
import com.revature.instances.Application;
import com.revature.instances.User;
import com.revature.util.ConnectionUtil;
import com.revature.util.LoggerUtil;

public class ApplicationDAOImpl implements ApplicationDAO {


	@Override
	public List<Application> findAll() {
		List<Application> list = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM application";
			
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int userid = rs.getInt("user_id");
				int accid = rs.getInt("account_id");
				
				Application a = new Application(new UserDAOImpl().findById(userid), accid);
				
				list.add(a);
			}
			stmt.close();
		} catch (SQLException e) {
			LoggerUtil.log.error("SQLException AccountDAOImpl");
		}
		
		return list;
	}

	@Override
	public boolean applyFor(int userid, int accid) {
		try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "{call insert_into_apps(?,?) }";
			
			CallableStatement stmt = conn.prepareCall(sql);
			
			stmt.setInt(1, userid);
			stmt.setInt(2,  accid);
			stmt.execute();
			stmt.close();
			return true;
			}catch (SQLException e) {
				LoggerUtil.getLogger().info("SQL Exception in ApplicationDAOImpl: update");
			}
			return false;
	}
	
	@Override
	public boolean applyFor(int userid) {
		try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "{call insert_into_apps(?, ?) }";
			
			CallableStatement stmt = conn.prepareCall(sql);
			
			stmt.setInt(1, userid);
			stmt.setInt(2, 0);
			stmt.execute();
			stmt.close();
			return true;
			}catch (SQLException e) {
				LoggerUtil.getLogger().info("SQL Exception in ApplicationDAOImpl: update");
			}
			return false;
	}

	@Override
	public boolean approve(Application application) {
try (Connection conn = ConnectionUtil.getConnection()) {
			if(application.getAccid() != 0) {
				
			String sql = "{ call approve_old_account(?, ?) }";
			
			CallableStatement stmt = conn.prepareCall(sql);
		
			stmt.setInt(1, application.getUser().getId());
			stmt.setInt(2, application.getAccid());
			stmt.execute();
			return true;
			}
			else {
				String sql = "{ call approve_new_account(?) }";
				
				CallableStatement stmt = conn.prepareCall(sql);
				
				stmt.setInt(1, application.getUser().getId());
				stmt.execute();
				stmt.close();
				return true;
			}
		} catch (SQLException e) {
			LoggerUtil.log.info("SQL Exception in ApplicationDaoImpl");
		}
		
		return false;
	}

	@Override
	public boolean deny(Application application) {
		try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "DELETE FROM APPLICATION WHERE user_id = " + application.getUser().getId() + " AND account_id = " + application.getAccid();
			
			Statement stmt = conn.createStatement();
			
			stmt.execute(sql);
			stmt.close();
			return true;
			}catch (SQLException e) {
				LoggerUtil.getLogger().info("SQL Exception in ApplicationDAOImpl: update");
			}
			return false;
		
	}
}
