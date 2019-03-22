package com.easyhelp.application.service.applicationuser;

import com.easyhelp.application.model.users.ApplicationUser;
import com.easyhelp.application.repository.DoctorRepository;
import com.easyhelp.application.repository.DonationCenterPersonnelRepository;
import com.easyhelp.application.repository.DonorRepository;
import com.easyhelp.application.repository.SystemAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SystemAdminRepository systemAdminRepository;

    @Autowired
    private DonationCenterPersonnelRepository donationCenterPersonnelRepository;


    public ApplicationUser findByEmailInAllUsers(String email) throws UsernameNotFoundException {
        ApplicationUser user = donorRepository.findByEmail(email);

        if (user == null)
            user = doctorRepository.findByEmail(email);

        if (user == null)
            user = donationCenterPersonnelRepository.findByEmail(email);

        if (user == null)
            user = systemAdminRepository.findByEmail(email);

        if (user == null)
            throw new UsernameNotFoundException(email);

        return user;
    }
}
