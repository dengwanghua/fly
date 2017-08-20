package com.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.UserMapper;
import com.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public User hasUser(User user) {
		// TODO Auto-generated method stub
		return userMapper.hasUser(user);
	}

	@Override
	public int registerUser(User user) {
		// TODO Auto-generated method stub
		return userMapper.registerUser(user);
	}

	@Override
	public User loginUser(User user) {
		// TODO Auto-generated method stub
		return userMapper.loginUser(user);
	}

	@Override
	public User userInfo(User user) {
		// TODO Auto-generated method stub
		return userMapper.userInfo(user);
	}

	@Override
	public int updatePassword(User user) {
		// TODO Auto-generated method stub
		return userMapper.updatePassword(user);
	}
	
	
	
}
