package com.molla.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.molla.common.entity"})
public class MollaFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(MollaFrontEndApplication.class, args);
	}

}
