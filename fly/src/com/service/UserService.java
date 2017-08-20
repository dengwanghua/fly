package com.service;

import java.util.List;

import com.pojo.User;

public interface UserService {
	
	public User hasUser(User user);
	public int registerUser(User user);
	public User loginUser(User user);
	public User userInfo(User user);
	public int updatePassword(User user);
}
