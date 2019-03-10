package com.easyhelp.application.controller;

import com.easyhelp.application.model.users.ApplicationUser;
import com.easyhelp.application.service.RegisterService;
import com.easyhelp.application.utils.response.Response;
import com.easyhelp.application.utils.response.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class AuthenticationController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RegisterService registerService;

    public AuthenticationController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Response> signUp(@RequestBody ApplicationUser applicationUser) {
        applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));
        String error = registerService.registerUser(applicationUser);
        if (error.equals("")) {
            return ResponseBuilder.encode(HttpStatus.OK);
        }
        return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, error);
    }

}