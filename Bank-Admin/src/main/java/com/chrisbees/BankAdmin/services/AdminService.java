package com.chrisbees.BankAdmin.services;

import com.chrisbees.BankAdmin.dto.LoginDTO;
import com.chrisbees.BankAdmin.model.Admin;
import com.chrisbees.BankAdmin.repository.AdminRepository;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final AdminRepository repository;

    public Handler<RoutingContext> registerAdmin() {
        return  ctx -> {
            Admin admin = ctx.body().asPojo(Admin.class);
            ctx.vertx().executeBlocking(() -> {
                Optional<Admin> findUser = repository.findByUsername(admin.getUsername());
                log.info("retrieving admin");
                return findUser;
            }).onComplete(res -> {
                if (res.result().isPresent()){
                    log.error("User already exists");
                    return;
                }
                log.info("creating User");
                ctx.vertx().executeBlocking(() -> repository.save(admin));
                log.info("Successfully created user");
                ctx.response().setStatusCode(200).end(Json.encodePrettily(admin));
            });
        };

    }

    public Handler<RoutingContext> loginAdmin() {
        return ctx -> {
            LoginDTO adminLoginBody = ctx.body().asPojo(LoginDTO.class);
            ctx.vertx().executeBlocking(() -> {
                Optional<Admin> byUsername = repository.findByUsername(adminLoginBody.getUsername());
                log.info("Checking for user");
                return byUsername;
            }).onComplete(res -> {
                if (res.result().isEmpty() || !adminLoginBody.getPassword().equals(res.result().get().getPassword())){
                    log.error("User does not exists");
                    return;
                }
                log.info("Logging in");
                ctx.response().setStatusCode(200).end("login successful");
                log.info("login successful");
            });
        };
    }
//    public AdminDTO registerAdmin(Admin admin){
//        User user = new User();
////        Authentication auth = new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword());
//        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
//        admin.setRole(admin.getRole());
//        user.setFirstName(admin.getFirstname());
//        user.setLastName(admin.getLastname());
//        user.setEmail(admin.getEmail());
//        user.setUsername(admin.getUsername());
//        user.setPassword(admin.getPassword());
//        user.setPhoneNumber(admin.getPhoneNumber());
//        user.setRole(admin.getRole());
//        user.setAdmin(admin);
//        admin.setUser(user);
//        String token = tokenService.generateToken(admin.getUser());
//        var admin1 = adminRepository.save(admin);
//        return new AdminDTO(admin1, token);
//
//    }
}
