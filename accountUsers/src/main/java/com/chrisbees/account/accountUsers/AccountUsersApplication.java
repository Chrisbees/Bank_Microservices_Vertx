package com.chrisbees.account.accountUsers;

import io.vertx.core.Vertx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountUsersApplication.class, args);
	}

	@Bean
	public Vertx vertx(){
		return Vertx.vertx();
	}

}
