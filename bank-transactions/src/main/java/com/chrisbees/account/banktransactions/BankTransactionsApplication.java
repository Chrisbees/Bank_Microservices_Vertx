package com.chrisbees.account.banktransactions;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankTransactionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankTransactionsApplication.class, args);
	}

	@Bean
	public Vertx vertx(){
		return Vertx.vertx();
	}

	@Bean
	public WebClient webClient(){
		return WebClient.create(vertx());
	}

}
