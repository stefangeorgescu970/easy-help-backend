package com.easyhelp.application.service;

import com.easyhelp.application.model.users.ApplicationUser;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.model.users.UserType;
import com.easyhelp.application.repository.DoctorRepository;
import com.easyhelp.application.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class RegisterService {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    public String registerUser(ApplicationUser user) {
        if (user.getUserType() == UserType.DONOR) {
            if (donorRepository.findByEmail(user.getEmail()) != null)
                return "userAlredyRegisterd";
            else {
                Donor donor = new Donor();
                donor.setEmail(user.getEmail());
                donor.setPassword(user.getPassword());
                donor.setUserType(user.getUserType());
                donorRepository.save(donor);
            }
        } else {
            if (doctorRepository.findByEmail(user.getEmail()) != null)
                return "userAlredyRegisterd";
            else {
                Doctor donor = new Doctor();
                donor.setEmail(user.getEmail());
                donor.setPassword(user.getPassword());
                donor.setUserType(user.getUserType());
                doctorRepository.save(donor);
            }
        }
        return "";
    }
}
