package com.easyhelp.application.service;

import com.easyhelp.application.model.users.ApplicationUser;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.DoctorRepository;
import com.easyhelp.application.repository.DonorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private DonorRepository donorRepository;
    private DoctorRepository doctorRepository;

    public UserDetailsServiceImpl(DonorRepository donorRepository, DoctorRepository doctorRepository) {
        this.donorRepository = donorRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ApplicationUser applicationUser = doctorRepository.findByEmail(email);

        if (applicationUser == null) {
            applicationUser = donorRepository.findByEmail(email);
            if (applicationUser == null){
                throw new UsernameNotFoundException(email);
            }
        }
        return new User(applicationUser.getEmail(), applicationUser.getPassword(), emptyList());
    }

    public ApplicationUser getUserDetails(String username) throws UsernameNotFoundException {

        Donor donor = donorRepository.findByEmail(username);
        if (donor != null) {
            return donor;
        }

        Doctor doctor = doctorRepository.findByEmail(username);
        if (doctor != null)
            return doctor;

        throw new UsernameNotFoundException(username);
    }
}