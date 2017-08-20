package com.dao;

import java.util.List;

import com.pojo.User;

public interface UserMapper {
	User hasUser(User user);
	int registerUser(User user);
	User loginUser(User user);
	User userInfo(User user);
	int updatePassword(User user);
}
