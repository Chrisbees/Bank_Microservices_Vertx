package com.chrisbees.account.accountUsers.service;

import com.chrisbees.account.accountUsers.model.AccountUser;
import com.chrisbees.account.accountUsers.repository.AccountUserRepository;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountUserService {

    private final AccountUserRepository repository;

    public Handler<RoutingContext> createAccountUser() {
        return ctx -> {
            AccountUser accountUser = ctx.body().asPojo(AccountUser.class);
            ctx.vertx().executeBlocking(() -> {
                Optional<AccountUser> byUsername = repository.findByUsername(accountUser.getUsername());
                log.info("retrieving user");
                return byUsername;
            }).onComplete(res -> {
                if (res.result().isPresent()){
                    log.error("User already exists");
                    ctx.response().end("User already exists");
                    return;
                }
                ctx.vertx().executeBlocking(() -> {
                    log.info("Saving User");
                   return repository.save(accountUser);
                });
                ctx.response().setStatusCode(200).end(Json.encodePrettily(accountUser));
            });
        };
    }

    public Handler<RoutingContext> getAllUsers() {
        return ctx -> {
            ctx.vertx().executeBlocking(() -> {
                log.info("Retrieving list of users");
                return repository.findAll();
            }).onComplete(res -> {
                if (res.result().isEmpty()){
                    log.info("There are no users in the database");
                    ctx.response().end("There are no users in the database");
                    return;
                }
                ctx.response().setStatusCode(200).end(Json.encodePrettily(res.result()));
            });
        };
    }

    public Handler<RoutingContext> getUserById() {
        return ctx -> {
            Integer userId = Integer.parseInt(ctx.pathParam("userId"));
            ctx.vertx().executeBlocking(() -> {
                log.info("Checking for user");
                return repository.findById(userId);
            }).onComplete(res -> {
                if (res.result().isEmpty()){
                    log.error("user does not exist");
                    ctx.response().end("user does not exist");
                }
                log.info("user successfully retrieved");
                AccountUser user = res.result().get();
                ctx.response().setStatusCode(200).end(Json.encodePrettily(user));
            });
        };
    }

    public Handler<RoutingContext> deleteUserById() {
        return ctx -> {
            Integer userId = Integer.parseInt(ctx.pathParam("userId"));
            ctx.vertx().executeBlocking(() -> {
                log.info("retrieving user");
                return repository.findById(userId);
            }).onComplete(res -> {
                if (res.result().isEmpty()){
                    log.info("Theres no user with id {}", userId);
                    ctx.response().end("user does not exist");
                }
                ctx.vertx().executeBlocking(() -> {
                    log.info("Deleting User");
                   repository.deleteById(userId);;
                    return userId;
                });
                ctx.response().setStatusCode(200).end("User successfully Deleted");
            });
        };
    }

    public Handler<RoutingContext> updateAccountUser() {
        return ctx -> {
            AccountUser accountUser = ctx.body().asPojo(AccountUser.class);
            Integer userId = Integer.parseInt(ctx.pathParam("userId"));
            ctx.vertx().executeBlocking(() -> {
                log.info("checking for user");
                return repository.findById(userId);
            }).onComplete(res -> {
                if (res.result().isEmpty()){
                    log.info("user does not exist");
                    ctx.response().end("user does not exist");
                }
                AccountUser user = res.result().get();
                user.setEmail(accountUser.getEmail());
                user.setRole(accountUser.getRole());
                user.setPassword(accountUser.getPassword());
                user.setFirstName(accountUser.getFirstName());
                user.setUsername(accountUser.getUsername());
                user.setLastName(accountUser.getLastName());
                user.setPhoneNumber(accountUser.getPhoneNumber());
                ctx.vertx().executeBlocking(() -> {
                    log.info("Saving updated User");
                    AccountUser save = repository.save(user);
                    return save;
                }).onComplete(result -> {
                    if (result.failed()) {
                        log.info("failed to updated user: {0}", result.cause());
                        ctx.response().end("failed to update user");
                    }
                    log.info("user updated successfully");
                    ctx.response().setStatusCode(200).end(Json.encodePrettily(user));
                });
            });
        };
    }
}
