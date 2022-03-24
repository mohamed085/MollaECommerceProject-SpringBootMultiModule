package com.molla.admin.controller;

import com.molla.admin.dto.ProductDTO;
import com.molla.common.entity.Product;
import com.molla.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import com.molla.admin.service.ProductService;

@RestController
public class ProductRestController {

	@Autowired 
	private ProductService service;

	@PostMapping("/products/check_unique")
	public String checkUnique(@Param("id") Integer id, @RequestParam("name") String name) {
		return service.checkUnique(id, name);
	}

	@GetMapping("/products/get/{id}")
	public ProductDTO getProductInfo(@PathVariable("id") Integer id)
			throws ProductNotFoundException {

		Product product = service.get(id);

		return new ProductDTO(product.getName(), product.getMainImagePath(),
				product.getDiscountPrice(), product.getCost());
	}
}