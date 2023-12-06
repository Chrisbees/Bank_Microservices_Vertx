package com.chrisbees.account.bank.accounts.controller;

import com.chrisbees.account.bank.accounts.service.BankServices;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BankController {

    private final Vertx vertx;
    private final BankServices services;

    @Bean
    public Router router(){
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/create-account/:userId").handler(services.createBankAccount());
        router.get("/get-account/:accountNumber").handler(services.getAccountByAccountNumber());
        router.delete("/delete-account/:bankId").handler(services.deleteAccount());
        router.patch("/patch-update/:bankId").handler(services.patchBankDetailsUpdate());

        return router;
    }
}
