package com.chrisbees.account.banktransactions.services;

import com.chrisbees.account.banktransactions.dto.BankAccountDTO;
import com.chrisbees.account.banktransactions.dto.BankPatchDto;
import com.chrisbees.account.banktransactions.model.Transaction;
import com.chrisbees.account.banktransactions.repository.TransactionRepository;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServices {

    private final WebClient client;
    private final TransactionRepository repository;
    public Handler<RoutingContext> performTransaction() {
        return ctx -> {
            Transaction transaction = ctx.body().asPojo(Transaction.class);
            Future<HttpResponse<Buffer>> sender = getUserAccount(transaction.getSenderAccount());
            sender.onComplete(getSender -> {
                if (getSender.succeeded()) {
                    BankAccountDTO senderAccount = getSender.result().bodyAsJson(BankAccountDTO.class);
                    log.info(senderAccount.toString());
                    transaction.setSenderName(senderAccount.getAccountName());
                    Future<HttpResponse<Buffer>> receiver = getUserAccount(transaction.getReceiverAccount());
                    receiver.onComplete(getReceiver -> {
                        if (getSender.succeeded()){
                            BankAccountDTO receiverAccount = getReceiver.result().bodyAsJson(BankAccountDTO.class);
                            log.info(receiverAccount.toString());
                            transaction.setReceiverName(receiverAccount.getAccountName());

                            if (transaction.getAmount() > senderAccount.getBalance()){
                                log.info("Insufficient Funds");
                            }else {
                                senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());
                                receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getAmount());

                                sendPatchUpdate(senderAccount);
                                sendPatchUpdate(receiverAccount);

                                ctx.vertx().executeBlocking(() -> {
                                    Transaction saveSender = new Transaction();
                                    Transaction saveReceiver = new Transaction();
                                    saveSender.setDescription(transaction.getDescription());
                                    saveSender.setTransactionDateTime(LocalDateTime.now());
                                    saveSender.setReceiverName(receiverAccount.getAccountName());
                                    saveSender.setTransactionType("Debit");
                                    saveSender.setSenderName(senderAccount.getAccountName());
                                    saveSender.setSenderAccount(senderAccount.getAccountNumber());
                                    saveSender.setAmount(transaction.getAmount());
                                    saveSender.setBankAccountId(senderAccount.getId());
                                    saveSender.setStatus(transaction.getStatus());
                                    saveSender.setReceiverAccount(receiverAccount.getAccountNumber());

                                    saveReceiver.setDescription(transaction.getDescription());
                                    saveReceiver.setTransactionDateTime(LocalDateTime.now());
                                    saveReceiver.setTransactionType("Credit");
                                    saveReceiver.setSenderName(senderAccount.getAccountName());
                                    saveReceiver.setSenderAccount(senderAccount.getAccountNumber());
                                    saveReceiver.setAmount(transaction.getAmount());
                                    saveReceiver.setBankAccountId(receiverAccount.getId());
                                    saveReceiver.setStatus(senderAccount.getStatus());
                                    repository.save(saveReceiver);
                                    return repository.save(saveSender);
                                }).onComplete(res -> {
                                    if (res.succeeded()){
                                        log.info("Transaction saved in database");
                                    } else {
                                        log.info("Could not persist transaction");
                                    }
                                });
                                log.info("transaction Successful");
                                ctx.response().end("Transaction Successful");
                            }
                        }else {
                            log.info("failed to get receiver");
                        }
                    });
                }else {
                    log.error("Failed to get sender");
                }

            });

        };
    }

    public Handler<RoutingContext> performDeposit() {
        return ctx -> {
            Transaction transaction = ctx.body().asPojo(Transaction.class);
                    Future<HttpResponse<Buffer>> receiver = getUserAccount(transaction.getReceiverAccount());
                    receiver.onComplete(getReceiver -> {
                        if (receiver.succeeded()){
                            BankAccountDTO receiverAccount = getReceiver.result().bodyAsJson(BankAccountDTO.class);
                                receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getAmount());
                                sendPatchUpdate(receiverAccount);
                                ctx.vertx().executeBlocking(() -> {
                                    Transaction saveReceiver = new Transaction();
                                    saveReceiver.setDescription(transaction.getDescription());
                                    saveReceiver.setTransactionDateTime(LocalDateTime.now());
                                    saveReceiver.setReceiverName(receiverAccount.getAccountName());
                                    saveReceiver.setTransactionType("Deposit");
                                    saveReceiver.setAmount(transaction.getAmount());
                                    saveReceiver.setStatus(transaction.getStatus());
                                    saveReceiver.setReceiverAccount(receiverAccount.getAccountNumber());
                                    return repository.save(saveReceiver);
                                }).onComplete(res -> {
                                    if (res.succeeded()){
                                        log.info("Transaction saved in database");
                                    } else {
                                        log.info("Could not persist transaction");
                                    }
                                });
                                log.info("Deposit Successful");
                                ctx.response().end("Deposit Successful");
                        }else {
                            log.info("failed to get receiver");
                        }
                });
        };
    }

    private Future<HttpResponse<Buffer>> getUserAccount(String account) {
       return client.get(9002, "localhost", "/get-account/" + account)
                .send()
                .onComplete(res -> {
                    if (res.succeeded()){
                        log.info("account retrieved");
                    }else {
                        log.error("failed to retrieve account");
                    }
                });
    }

    private void sendPatchUpdate(BankAccountDTO account) {
        client.patch(9002, "localhost", "/patch-update/" + account.getId())
                .sendJson(account)
                .onComplete(res -> {
                    if (res.succeeded()) {
                        log.info("account update successful");
                    } else {
                        log.error("failed to update account");
                    }
                });
    }
}
