package com.easyhelp.application.controller;

import com.easyhelp.application.model.users.ApplicationUser;
import com.easyhelp.application.security.JwtTokenProvider;
import com.easyhelp.application.service.RegisterService;
import com.easyhelp.application.utils.exceptions.UserAlreadyRegisteredException;
import com.easyhelp.application.utils.response.Response;
import com.easyhelp.application.utils.response.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/users")
public class AuthenticationController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RegisterService registerService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Response> signUp(@RequestBody ApplicationUser applicationUser) {
        applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));

        try {
            registerService.registerUser(applicationUser);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (UserAlreadyRegisteredException exception) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity signin(@RequestBody ApplicationUser data) {

        try {
            String username = data.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.registerService.findByEmail(data).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("email", username);
            model.put("token", token);
            model.put("roles", this.registerService.findByEmail(data).getRoles());
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }



}