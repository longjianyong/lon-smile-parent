package com.lon.smile.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lon.smile.framework.dao.UserInfoMapper;
import com.lon.smile.framework.model.base.UserInfo;
import com.lon.smile.framework.service.UserIF;


@Service("userService")
public class UserImpl implements UserIF{

	@Autowired
	private UserInfoMapper userInfoMapper;
	
	public UserInfo getUserById(int id) {
		UserInfo info = new UserInfo();
		info.setId(10);
		return info;
	}

	public List<UserInfo> getUsers() {
		return null;
	}

	public int insert(UserInfo userInfo) {
		return userInfoMapper.insert(userInfo);
	}

}
