package com.likg.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.likg.auth.domain.User;

public class AuthenticationHelper {

	/**
	 * 通过security容器获取当前用户的信息
	 * @return 当前用户对象
	 */
	public static User getCurrentUser() {
		User currentUser = null;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null && auth.getPrincipal() instanceof UserDetails) {
			currentUser = (User) auth.getPrincipal();
		}
		return currentUser;
	}

}
