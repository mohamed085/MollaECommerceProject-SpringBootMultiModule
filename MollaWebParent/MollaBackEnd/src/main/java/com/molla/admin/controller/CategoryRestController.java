package com.shopme.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.molla.admin.service.CategoryService;

@RestController
public class CategoryRestController {

	private final CategoryService service;

	public CategoryRestController(CategoryService service) {
		this.service = service;
	}

	@PostMapping("/categories/check_unique")
	public String checkUnique(@RequestParam("id") Integer id, @RequestParam("name") String name,
			@RequestParam("alias") String alias) {
		return service.checkUnique(id, name, alias);
	}
}
