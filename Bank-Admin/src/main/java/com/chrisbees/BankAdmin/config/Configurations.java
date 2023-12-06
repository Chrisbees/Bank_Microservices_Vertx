package com.chrisbees.BankAdmin.config;

import com.chrisbees.BankAdmin.MainVerticle;
import io.vertx.core.Vertx;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

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
