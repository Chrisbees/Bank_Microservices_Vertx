package com.chrisbees.BankAdmin;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class BankAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAdminApplication.class, args);
	}

	@Bean
	public Vertx vertx(){
		return Vertx.vertx();
	}

}
