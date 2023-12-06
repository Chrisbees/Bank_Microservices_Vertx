package com.chrisbees.account.bank.accounts.service;

import com.chrisbees.account.bank.accounts.dto.BankAccountWithUserResponse;
import com.chrisbees.account.bank.accounts.dto.UserDto;
import com.chrisbees.account.bank.accounts.model.BankAccount;
import com.chrisbees.account.bank.accounts.repository.BankAccountRepository;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankServices {

    private final BankAccountRepository repository;
    private final WebClient client;

    public Handler<RoutingContext> createBankAccount() {
        return ctx -> {
            int userId = Integer.parseInt(ctx.pathParam("userId"));
            BankAccount account = ctx.body().asPojo(BankAccount.class);
            ctx.vertx().executeBlocking(() -> {
                log.info("saving bank account");
                account.setStatus("pending");
                account.setUserId(userId);
                account.setAccountNumber(generateRandomAccountNumber());
                return repository.save(account);
            }).onComplete(result -> {
                if (result.succeeded()){
                    log.info("Account Creation Successful");
                    Future<HttpResponse<Buffer>> response = getHttpResponseFuture(userId);
                    response.onComplete(res -> {
                        if (res.succeeded()){
                            UserDto userDto = res.result().bodyAsJson(UserDto.class);
                            BankAccountWithUserResponse buildAccount = BankAccountWithUserResponse.builder()
                                    .id(account.getId())
                                    .accountNumber(account.getAccountNumber())
                                    .accountType(account.getAccountType())
                                    .balance(account.getBalance())
                                    .status(account.getStatus())
                                    .user(userDto)
                                    .build();
                            ctx.response().setStatusCode(200).end(Json.encodePrettily(buildAccount));
                        }
                    });
                            }else {
                    ctx.response().setStatusCode(500).end("Error saving bank account");
                }
                        });
                    };
        }

    private String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }

    private Future<HttpResponse<Buffer>> getHttpResponseFuture(int userId) {
        return client.get(9001, "localhost", "/user/get/" + userId)
                .send()
                .onComplete(res -> {
                    if (res.failed()) {
                        log.info("could not retrieve user");
                    }
                    UserDto users = res.result().bodyAsJson(UserDto.class);
                });
    }


    public Handler<RoutingContext> getAccountByAccountNumber() {
        return ctx -> {
            String accountNumber = ctx.pathParam("accountNumber");
            ctx.vertx().executeBlocking(() -> {
                log.info("fetching user by account number");
               return repository.findByAccountNumber(accountNumber);
            }).onComplete(res -> {
                if (res.succeeded() && res.result().isPresent()){
                    log.info("account found in database retrieving response");
                    BankAccount account = res.result().get();
                    Future<HttpResponse<Buffer>> response = getHttpResponseFuture(account.getUserId());
                    response.onComplete(httpRes -> {
                        if (httpRes.succeeded()){
                            log.info("account retrieved successfully");
                            UserDto userDto = httpRes.result().bodyAsJson(UserDto.class);
                            account.setStatus("pending");
                            BankAccountWithUserResponse buildAccount = BankAccountWithUserResponse.builder()
                                    .id(account.getId())
                                    .accountNumber(account.getAccountNumber())
                                    .accountType(account.getAccountType())
                                    .balance(account.getBalance())
                                    .status(account.getStatus())
                                    .user(userDto)
                                    .build();
                            ctx.response().setStatusCode(200)
                                    .end(Json.encodePrettily(buildAccount));
                        }else {
                            log.error("failed to retrieve account");
                            ctx.response().setStatusCode(500).end("failed to retrieve account");
                        }
                    });
                }else {
                    log.info("Account Not Found");
                    ctx.response().setStatusCode(404).end("Account Not Found");
                }
            });
        };
    }

    public Handler<RoutingContext> deleteAccount() {
        return ctx -> {
            int bankId = Integer.parseInt(ctx.pathParam("bankId"));
            ctx.vertx().executeBlocking(() -> {
               repository.deleteById(bankId);
               return ctx;
            }).onComplete(res -> {
                if (res.succeeded()) {
                    ctx.response().setStatusCode(200).end("Account Successfully Deleted");
                }else {
                    ctx.response().setStatusCode(500).end("Account deletion failed");
                }
            });
        };
    }


    public Handler<RoutingContext> patchBankDetailsUpdate() {
        return ctx -> {
            int bankId = Integer.parseInt(ctx.pathParam("bankId"));
            BankAccountWithUserResponse account = ctx.body().asPojo(BankAccountWithUserResponse.class);
            ctx.vertx().executeBlocking(() -> {
                log.info("checking if the account exists");
                Optional<BankAccount> findAccount = repository.findById(bankId);
                log.info("the account exists");
                return findAccount;
            }).onComplete(res -> {
                if (res.result().isPresent()){
                    BankAccount bankAccount = res.result().get();
                    bankAccount.setAccountNumber(account.getAccountNumber());
                    bankAccount.setAccountName(account.getAccountName());
                    bankAccount.setStatus(account.getStatus());
                    bankAccount.setAccountType(account.getAccountType());
                    bankAccount.setBalance(account.getBalance());
                    ctx.vertx().executeBlocking(() -> repository.save(bankAccount))
                            .onComplete(response -> {
                                if (response.succeeded()){
                                    log.info("Updated Account");
                                    ctx.response().end("Updated Account");
                                }else {
                                    log.error("Update failed");
                                    ctx.response().end("Updated Failed");
                                }
                            });
                }else {
                    log.info("Account does not exists");
                    ctx.response().end("Account does not exists");
                }
            });
        };
    }
}
