package com.train.service.impl;

import java.sql.Statement; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.train.beans.TrainException;
import com.train.beans.UserBean;
import com.train.constant.ResponseCode;
import com.train.constant.UserRole;
import com.train.service.UserService;
import com.train.utility.DBUtil;

public class UserServiceImpl implements UserService {

	private final String TABLE_NAME;

	public UserServiceImpl(UserRole userRole) {
		if (userRole == null) {
			userRole = UserRole.CUSTOMER;
		}
		this.TABLE_NAME = userRole.toString();
	}

	@Override
	public UserBean getUserByEmailId(String customerEmailId) throws TrainException {
	    UserBean customer = null;
	    String query = "SELECT * FROM " + TABLE_NAME + " WHERE MAILID=?";
	    try {
	        Connection con = DBUtil.getConnection();
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, customerEmailId);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            customer = new UserBean();
	            customer.setFName(rs.getString("FNAME")); // Use uppercase column names for MySQL
	            customer.setLName(rs.getString("LNAME"));
	            customer.setAddr(rs.getString("ADDR"));
	            customer.setMailId(rs.getString("MAILID"));
	            customer.setPhNo(rs.getLong("PHNO"));
	        } else {
	            throw new TrainException(ResponseCode.NO_CONTENT);
	        }
	        ps.close();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	        throw new TrainException(e.getMessage());
	    }
	    return customer;
	}

	@Override
	public List<UserBean> getAllUsers() throws TrainException {
	    List<UserBean> customers = null;
	    String query = "SELECT * FROM " + TABLE_NAME;
	    try {
	        Connection con = DBUtil.getConnection();
	        PreparedStatement ps = con.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();
	        customers = new ArrayList<>();
	        while (rs.next()) {
	            UserBean customer = new UserBean();
	            customer.setFName(rs.getString("FNAME"));
	            customer.setLName(rs.getString("LNAME"));
	            customer.setAddr(rs.getString("ADDR"));
	            customer.setMailId(rs.getString("MAILID"));
	            customer.setPhNo(rs.getLong("PHNO"));
	            customers.add(customer);
	        }

	        if (customers.isEmpty()) {
	            throw new TrainException(ResponseCode.NO_CONTENT);
	        }
	        ps.close();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	        throw new TrainException(e.getMessage());
	    }
	    return customers;
	}

	@Override
	public String updateUser(UserBean customer) {
	    String responseCode = ResponseCode.FAILURE.toString();
	    String query = "UPDATE " + TABLE_NAME + " SET FNAME=?, LNAME=?, ADDR=?, PHNO=? WHERE MAILID=?";
	    try {
	        Connection con = DBUtil.getConnection();
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, customer.getFName());
	        ps.setString(2, customer.getLName());
	        ps.setString(3, customer.getAddr());
	        ps.setLong(4, customer.getPhNo());
	        ps.setString(5, customer.getMailId());
	        int response = ps.executeUpdate();
	        if (response > 0) {
	            responseCode = ResponseCode.SUCCESS.toString();
	        }
	        ps.close();
	    } catch (SQLException | TrainException e) {
	        responseCode += " : " + e.getMessage();
	    }
	    return responseCode;
	}

	@Override
	public String deleteUser(UserBean customer) {
	    String responseCode = ResponseCode.FAILURE.toString();
	    String query = "DELETE FROM " + TABLE_NAME + " WHERE MAILID=?";
	    try {
	        Connection con = DBUtil.getConnection();
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, customer.getMailId());

	        int response = ps.executeUpdate();
	        if (response > 0) {
	            responseCode = ResponseCode.SUCCESS.toString();
	        }
	        ps.close();
	    } catch (SQLException | TrainException e) {
	        responseCode += " : " + e.getMessage();
	    }
	    return responseCode;
	}

	@Override
	public String registerUser(UserBean customer) {
	    String responseCode = ResponseCode.FAILURE.toString();
	    String query = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?,?)";
	    try {
	        Connection con = DBUtil.getConnection();
	        PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Specify RETURN_GENERATED_KEYS
	        ps.setString(1, customer.getMailId());
	        ps.setString(2, customer.getPWord());
	        ps.setString(3, customer.getFName());
	        ps.setString(4, customer.getLName());
	        ps.setString(5, customer.getAddr());
	        ps.setLong(6, customer.getPhNo());
	        int rowsAffected = ps.executeUpdate(); // Use executeUpdate() for INSERT
	        if (rowsAffected > 0) {
	            responseCode = ResponseCode.SUCCESS.toString();
	        }
	        ResultSet generatedKeys = ps.getGeneratedKeys(); // Retrieve auto-generated keys if needed
	        if (generatedKeys.next()) {
	            // Handle generated keys if required
	        }
	        ps.close();
	    } catch (SQLException | TrainException e) {
	        if (e.getMessage().toUpperCase().contains("ORA-00001")) {
	            responseCode += " : " + "User With Id: " + customer.getMailId() + " is already registered ";
	        } else {
	            responseCode += " : // " + e.getMessage();
	        }
	    }
	    return responseCode;
	}
	
	@Override
	public String addComplaint(String userEmailId, long trainNo, String fromStn, String toStn, String description) {
	    String responseCode = ResponseCode.FAILURE.toString();
	    String query = "INSERT INTO complaints (user_email, train_number, from_station, to_station, description) VALUES(?,?,?,?,?)";
	    try {
	        Connection con = DBUtil.getConnection();
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, userEmailId);
	        ps.setLong(2, trainNo);
	        ps.setString(3, fromStn);
	        ps.setString(4, toStn);
	        ps.setString(5, description);
	        int rowsAffected = ps.executeUpdate();
	        if (rowsAffected > 0) {
	            responseCode = ResponseCode.SUCCESS.toString();
	        }
	        ps.close();
	    } catch (SQLException | TrainException e) {
	        responseCode += " : " + e.getMessage();
	    }
	    return responseCode;
	}
    
	@Override
	public String addFood(String userEmailId, long trainNo, String fromStn, String toStn, String food) {
	    String responseCode = ResponseCode.FAILURE.toString();
	    String query = "INSERT INTO food (user_email, train_number, from_station, to_station, food) VALUES(?,?,?,?,?)";
	    try {
	        Connection con = DBUtil.getConnection();
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, userEmailId);
	        ps.setLong(2, trainNo);
	        ps.setString(3, fromStn);
	        ps.setString(4, toStn);
	        ps.setString(5, food);
	        int rowsAffected = ps.executeUpdate();
	        if (rowsAffected > 0) {
	            responseCode = ResponseCode.SUCCESS.toString();
	        }
	        ps.close();
	    } catch (SQLException | TrainException e) {
	        responseCode += " : " + e.getMessage();
	    }
	    return responseCode;
	}



	@Override
	public UserBean loginUser(String username, String password) throws TrainException {
	    UserBean customer = null;
	    String query = "SELECT * FROM " + TABLE_NAME + " WHERE MAILID=? AND PWORD=?";
	    try {
	        Connection con = DBUtil.getConnection();
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, username);
	        ps.setString(2, password);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            customer = new UserBean();
	            customer.setFName(rs.getString("FNAME")); 
	            customer.setLName(rs.getString("LNAME"));
	            customer.setAddr(rs.getString("ADDR"));
	            customer.setMailId(rs.getString("MAILID"));
	            customer.setPhNo(rs.getLong("PHNO"));
	            customer.setPWord(rs.getString("PWORD"));
	        } else {
	            throw new TrainException(ResponseCode.UNAUTHORIZED);
	        }
	        ps.close();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	        throw new TrainException(e.getMessage());
	    }
	    return customer;
	}


}
