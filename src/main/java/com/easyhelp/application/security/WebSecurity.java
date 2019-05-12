package com.easyhelp.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@EnableWebSecurity
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                // all users
                .antMatchers( "/users/**").permitAll()
                .antMatchers( "/enums").permitAll()
                .antMatchers("/**").hasRole("SYS_ADMIN")

                // sys admin
                .antMatchers("/admin/**").hasRole("SYS_ADMIN")
                .antMatchers("/hospital/add").hasRole("SYS_ADMIN")
                .antMatchers("/hospital/remove").hasRole("SYS_ADMIN")
                .antMatchers("/hospital/getAllHospitals").hasRole("SYS_ADMIN")
                .antMatchers("/donationCenter/add").hasRole("SYS_ADMIN")
                .antMatchers("/donationCenter/remove").hasRole("SYS_ADMIN")
                .antMatchers("/donationCenter/getAll").hasRole("SYS_ADMIN")

                // doctor
                .antMatchers("/doctor/**").hasRole("DOCTOR")
                .antMatchers("/donor/checkPatientSSN").hasAnyRole( "DOCTOR, DONOR")

                // dcp
                .antMatchers("/donationCenter/getDCBookings").hasRole("DCP")
                .antMatchers("/donationCenter/getInCounty").hasRole("DCP")
                .antMatchers("/donationCenter/seeAllBloodRequests").hasRole("DCP")
                .antMatchers("/donation/addTestResult").hasRole("DCP")
                .antMatchers("/donation/addSplitResults").hasRole("DCP")
                .antMatchers("/donor/filterDonors").hasRole("DCP")
                .antMatchers("/donor/getInCounty").hasRole("DCP")


                //donor
                .antMatchers("/donor/**").hasRole("DONOR")
                .antMatchers("/donationCenter/createDonation").hasRole("DONOR")
                .antMatchers("/donationCenter/getAvailableHours").hasRole("DONOR")
                .antMatchers("/donationCenter/cancelBooking").hasRole("DONOR")
                .antMatchers("/donationCenter/getAll").hasRole("DONOR")


                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
        ;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}