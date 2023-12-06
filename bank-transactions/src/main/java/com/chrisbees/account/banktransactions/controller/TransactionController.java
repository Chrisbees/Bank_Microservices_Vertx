package com.chrisbees.account.banktransactions.controller;

import com.chrisbees.account.banktransactions.services.TransactionServices;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TransactionController {


    private final Vertx vertx;
    private final TransactionServices services;

    @Bean
    public Router router(){
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/credit").handler(services.performTransaction());
        router.post("/deposit").handler(services.performDeposit());
        return router;
    }
}
