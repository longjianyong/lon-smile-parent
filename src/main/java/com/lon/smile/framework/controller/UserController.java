package com.lon.smile.framework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lon.smile.framework.model.base.UserInfo;
import com.lon.smile.framework.service.UserIF;

@Controller
@RequestMapping("/user")
public class UserController  {
	@Autowired
	private UserIF userService;
	
	@RequestMapping("/showUserInfo/{userId}")
	public String showUserInfo(ModelMap modelMap, @PathVariable int userId){
		UserInfo userInfo = userService.getUserById(userId);
		modelMap.addAttribute("info", userInfo);
		return "/showInfo";
	}
}
