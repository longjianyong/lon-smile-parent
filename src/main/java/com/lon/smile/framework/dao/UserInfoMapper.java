package com.lon.smile.framework.dao;

import com.lon.smile.framework.model.base.UserInfo;

public interface UserInfoMapper {
    int insert(UserInfo record);

    int insertSelective(UserInfo record);
}