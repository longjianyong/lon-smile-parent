package com.lon.smile.framework.service;

import java.util.List;

import com.lon.smile.framework.model.base.UserInfo;
public interface UserIF {
	UserInfo getUserById(int id);

	List<UserInfo> getUsers();

	int insert(UserInfo userInfo);
}
