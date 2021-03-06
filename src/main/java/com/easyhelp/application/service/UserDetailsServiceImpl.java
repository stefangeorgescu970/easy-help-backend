package com.easyhelp.application.service;

import com.easyhelp.application.model.users.ApplicationUser;
import com.easyhelp.application.service.applicationuser.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ApplicationUserService applicationUserService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserService.findByEmailInAllUsers(email);

        return new User(applicationUser.getEmail(), applicationUser.getPassword(), applicationUser.getAuthorities());
    }
}