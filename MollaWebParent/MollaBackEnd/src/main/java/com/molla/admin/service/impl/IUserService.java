package com.molla.admin.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import com.molla.admin.error.UserNotFoundException;
import com.molla.admin.paging.PagingAndSortingHelper;
import com.molla.common.entity.Role;
import com.molla.common.entity.User;

public interface IUserService {

	public List<User> listAll();
	
	public List<Role> listRoles();
	
	public User save(User user);
	
	public boolean isEmailUnique(Integer id, String email);
	
	public User get(Integer id) throws UserNotFoundException;
	
	public void delete(Integer id) throws UserNotFoundException;
	
	public void updateUserEnabledStatus(Integer id, boolean enabled);
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	
	public User getByEmail(String email);
	
	public User updateAccount(User userInForm);
}
