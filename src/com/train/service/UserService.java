package com.train.service;

import java.util.List;

import com.train.beans.TrainException;
import com.train.beans.UserBean;

public interface UserService {

	public UserBean getUserByEmailId(String userEmailId) throws TrainException;

	public List<UserBean> getAllUsers() throws TrainException;

	public String updateUser(UserBean customer);

	public String deleteUser(UserBean customer);

	public String registerUser(UserBean customer);
	
	public UserBean loginUser(String username, String password) throws TrainException;
	
	public String addFood(String userEmailId, long trainNo, String fromStn, String toStn, String food) throws TrainException;

    public String addComplaint(String userEmailId, long trainNo, String fromStn, String toStn, String description) throws TrainException;

}
