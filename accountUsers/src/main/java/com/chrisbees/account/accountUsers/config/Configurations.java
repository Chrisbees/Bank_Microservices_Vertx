package com.chrisbees.account.accountUsers.config;

import com.chrisbees.account.accountUsers.MainVerticle;
import io.vertx.core.Vertx;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class Configurations {

    private final Vertx vertx;
    private final MainVerticle mainVerticle;

    @PostConstruct
    public void VertxConfig(){
            vertx.deployVerticle(mainVerticle);
        }

}
