package com.easyhelp.application.service;

import com.easyhelp.application.controller.AuthenticationController;
import com.easyhelp.application.model.users.*;
import com.easyhelp.application.repository.DoctorRepository;
import com.easyhelp.application.repository.DonationCenterPersonnelRepository;
import com.easyhelp.application.repository.DonorRepository;
import com.easyhelp.application.repository.SystemAdminRepository;
import com.easyhelp.application.utils.exceptions.UserAlreadyRegisteredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class RegisterService {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SystemAdminRepository systemAdminRepository;

    @Autowired
    private DonationCenterPersonnelRepository donationCenterPersonnelRepository;

    public void registerUser(ApplicationUser user) throws UserAlreadyRegisteredException {
        //TODO - this is really ugly code

        Set<String> role = new HashSet<>();
        role.add(user.getUserType().getRole());

        String email = user.getEmail();
        if (donorRepository.findByEmail(email) != null ||
                doctorRepository.findByEmail(email) != null ||
                donationCenterPersonnelRepository.findByEmail(email) != null ||
                systemAdminRepository.findByEmail(email) != null)
            throw new UserAlreadyRegisteredException("User already registered!");


        switch (user.getUserType()) {
            case DONOR: {
                Donor donor = new Donor();
                donor.setRoles(role);
                donor.setEmail(user.getEmail());
                donor.setPassword(user.getPassword());
                donor.setUserType(user.getUserType());
                donorRepository.save(donor);
            }
            break;
            case DOCTOR: {
                Doctor doctor = new Doctor();
                doctor.setRoles(role);
                doctor.setEmail(user.getEmail());
                doctor.setPassword(user.getPassword());
                doctor.setUserType(user.getUserType());
                doctor.setIsReviewed(false);
                doctor.setIsValid(false);
                doctorRepository.save(doctor);
            }
            break;
            case SYSADMIN: {
                SystemAdmin systemAdmin = new SystemAdmin();
                systemAdmin.setRoles(role);
                systemAdmin.setEmail(user.getEmail());
                systemAdmin.setPassword(user.getPassword());
                systemAdmin.setUserType(user.getUserType());
                systemAdminRepository.save(systemAdmin);
            }
            break;
            case DONATION_CENTER_PERSONNEL: {
                DonationCenterPersonnel donationCenterPersonnel = new DonationCenterPersonnel();
                donationCenterPersonnel.setRoles(role);
                donationCenterPersonnel.setEmail(user.getEmail());
                donationCenterPersonnel.setPassword(user.getPassword());
                donationCenterPersonnel.setUserType(user.getUserType());
                donationCenterPersonnel.setIsValid(false);
                donationCenterPersonnel.setIsReviewed(false);
                donationCenterPersonnelRepository.save(donationCenterPersonnel);
            }
        }
    }
}
