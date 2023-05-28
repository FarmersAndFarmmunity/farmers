package com.shop.farmers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FarmersApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmersApplication.class, args);
	}

}
