package com.chrisbees.BankAdmin.controller;

import com.chrisbees.BankAdmin.services.AdminService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Component
@Controller
public class AdminController {

    private final Vertx vertx;
    private final AdminService adminService;

    @Bean
    public Router router(){
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/admin/register").handler(adminService.registerAdmin());
        router.post("/admin/login").handler(adminService.loginAdmin());
        return router;
    }


//    @PostMapping("/register")
////    @PreAuthorize("hasRole('ADMIN')")
//    public AdminDTO createAdmin(@RequestBody Admin admin){
//        return adminService.registerAdmin(admin);
//    }
//
//    @PostMapping("/login")
//    public LoginDTO login(@RequestBody LoginRequest admin){
//        return loginRequest.login(admin);
//    }

//    @PostMapping("/verify-otp")
//    public LoginDTO verifyOtp(@RequestBody Otp otp){
//       return loginRequest.verifyOTP(otp.getUsername(), otp.getOtp(), otp.getPassword());
//    }

}

