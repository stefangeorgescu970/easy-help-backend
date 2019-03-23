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


@Service
public class RegisterService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

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

        switch (user.getUserType()) {
            case DONOR:
                if (donorRepository.findByEmail(user.getEmail()) != null)
                    throw new UserAlreadyRegisteredException("User already registered!");
                else {
                    Donor donor = new Donor();
                    donor.setRoles(user.getRoles());
                    logger.info(donor.getRoles().toString());
                    donor.setEmail(user.getEmail());
                    donor.setPassword(user.getPassword());
                    donor.setUserType(user.getUserType());
                    donorRepository.save(donor);
                }
                break;
            case DOCTOR:
                if (doctorRepository.findByEmail(user.getEmail()) != null)
                    throw new UserAlreadyRegisteredException("User already registered!");
                else {
                    Doctor doctor = new Doctor();
                    doctor.setRoles(user.getRoles());
                    doctor.setEmail(user.getEmail());
                    doctor.setPassword(user.getPassword());
                    doctor.setUserType(user.getUserType());
                    doctor.setIsReviewed(false);
                    doctor.setIsValid(false);
                    doctorRepository.save(doctor);
                }
                break;
            case SYSADMIN:
                if (systemAdminRepository.findByEmail(user.getEmail()) != null)
                    throw new UserAlreadyRegisteredException("User already registered!");
                else {
                    SystemAdmin systemAdmin = new SystemAdmin();
                    systemAdmin.setRoles(user.getRoles());

                    systemAdmin.setEmail(user.getEmail());
                    systemAdmin.setPassword(user.getPassword());
                    systemAdmin.setUserType(user.getUserType());
                    systemAdminRepository.save(systemAdmin);
                }
                break;
            case DONATION_CENTER_PERSONNEL:
                if (donationCenterPersonnelRepository.findByEmail(user.getEmail()) != null)
                    throw new UserAlreadyRegisteredException("User already registered!");
                else {
                    DonationCenterPersonnel donationCenterPersonnel = new DonationCenterPersonnel();
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
