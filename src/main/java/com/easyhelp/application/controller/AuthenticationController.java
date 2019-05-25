package com.easyhelp.application.controller;

import com.easyhelp.application.model.dto.account.AccountDTO;
import com.easyhelp.application.model.dto.account.RegisterDTO;
import com.easyhelp.application.model.dto.misc.IdentifierDTO;
import com.easyhelp.application.model.users.ApplicationUser;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.model.users.LoginResponse;
import com.easyhelp.application.model.users.PartnerUser;
import com.easyhelp.application.security.JwtTokenProvider;
import com.easyhelp.application.service.RegisterService;
import com.easyhelp.application.service.applicationuser.ApplicationUserService;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.response.Response;
import com.easyhelp.application.utils.response.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AuthenticationController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Response> signUp(@RequestBody RegisterDTO applicationUser) {
        applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));

        try {
            registerService.registerUser(applicationUser);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException exception) {
            return ResponseBuilder.encode(HttpStatus.OK, exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity signin(@RequestBody ApplicationUser data) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));

            ApplicationUser user = this.applicationUserService.findByEmailInAllUsers(data.getEmail());

            if (user instanceof PartnerUser) {
                PartnerUser partnerUser = (PartnerUser) user;
                if (!partnerUser.getIsReviewed())
                    return ResponseBuilder.encode(HttpStatus.OK, "Your account has not been validated yet. Try later or contact our email.");

                if (!partnerUser.getIsValid())
                    return ResponseBuilder.encode(HttpStatus.OK, "Your account has been deactivated. Please contact our email for info.");
            }

            AccountDTO accountDTO = new AccountDTO(user);

            String token = jwtTokenProvider.createToken(data.getEmail(), user.getRoles());
            return ResponseBuilder.encode(HttpStatus.OK, new LoginResponse(accountDTO, token));

        } catch (AuthenticationException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@RequestBody IdentifierDTO identifierDTO) {
        try {
            registerService.logoutDonor(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException exception) {
            return ResponseBuilder.encode(HttpStatus.OK, exception.getMessage());
        }
    }


}