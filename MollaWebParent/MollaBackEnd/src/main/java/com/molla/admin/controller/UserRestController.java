package com.molla.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.molla.admin.service.UserService;

@RestController
public class UserRestController {
	
	@Autowired
	private UserService service;
	
	@PostMapping("/users/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id, @RequestParam("email") String email) {
		return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
}
