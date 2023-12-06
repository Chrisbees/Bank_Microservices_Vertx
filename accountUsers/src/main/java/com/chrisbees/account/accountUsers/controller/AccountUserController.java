package com.chrisbees.account.accountUsers.controller;

import com.chrisbees.account.accountUsers.service.AccountUserService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AccountUserController {

    private final Vertx vertx;
    private final AccountUserService service;

    @Bean
    public Router router(){
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/user/register").handler(service.createAccountUser());
        router.get("/user/getAll").handler(service.getAllUsers());
        router.get("/user/get/:userId").handler(service.getUserById());
        router.delete("/user/delete/:userId").handler(service.deleteUserById());
        router.put("/user/update/:userId").handler(service.updateAccountUser());

        return router;
    }

}
