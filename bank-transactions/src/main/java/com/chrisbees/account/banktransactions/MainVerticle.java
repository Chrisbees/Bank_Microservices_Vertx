package com.chrisbees.account.banktransactions;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MainVerticle extends AbstractVerticle {

    private final Router router;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        vertx.createHttpServer().requestHandler(router)
                .listen(9003)
                .onSuccess(success -> {
                    log.info("Server successfully started on port {}", success.actualPort());
                    startPromise.complete();
                }).onFailure(fail -> startPromise.fail(fail.getCause()));
    }
}
