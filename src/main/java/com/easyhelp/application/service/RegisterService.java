package com.easyhelp.application.service;

import com.easyhelp.application.model.dto.auth.RegisterDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.model.users.SystemAdmin;
import com.easyhelp.application.repository.DoctorRepository;
import com.easyhelp.application.repository.DonationCenterPersonnelRepository;
import com.easyhelp.application.repository.DonorRepository;
import com.easyhelp.application.repository.SystemAdminRepository;
import com.easyhelp.application.service.applicationuser.ApplicationUserService;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.hospital.HospitalServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;
import com.easyhelp.application.utils.exceptions.UserAlreadyRegisteredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
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

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private HospitalServiceInterface hospitalService;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    public void registerUser(RegisterDTO user) throws UserAlreadyRegisteredException, EntityNotFoundException, SsnInvalidException {
        //TODO - this is really ugly code

        Set<String> role = new HashSet<>();
        role.add(user.getUserType().getRole());

        try {
            applicationUserService.findByEmailInAllUsers(user.getEmail());
            throw new UserAlreadyRegisteredException("A user with the email " + user.getEmail() + " already exists!");
        } catch (UsernameNotFoundException exception) {
            if (user.getSsn() != null) {
                String ssn = user.getSsn();
                MiscUtils.validateSsn(ssn);
            } else {
                throw new SsnInvalidException("No ssn was provided");
            }

            switch (user.getUserType()) {
                case DONOR: {
                    Donor donor = new Donor(user);
                    donor.setRoles(role);
                    donorRepository.save(donor);
                }
                break;
                case DOCTOR: {
                    Hospital hospital = hospitalService.findById(user.getLocationId());
                    Doctor doctor = new Doctor(user);

                    doctor.setHospital(hospital);
                    hospital.getDoctors().add(doctor);

                    doctor.setRoles(role);
                    doctorRepository.save(doctor);
                    hospitalService.save(hospital);
                }
                break;
                case SYSADMIN: {
                    SystemAdmin systemAdmin = new SystemAdmin(user);
                    systemAdmin.setRoles(role);
                    systemAdminRepository.save(systemAdmin);
                }
                break;
                case DONATION_CENTER_PERSONNEL: {
                    DonationCenter dc = donationCenterService.findById(user.getLocationId());
                    DonationCenterPersonnel donationCenterPersonnel = new DonationCenterPersonnel(user);

                    donationCenterPersonnel.setDonationCenter(dc);
                    dc.getDonationCenterPersonnelSet().add(donationCenterPersonnel);

                    donationCenterPersonnel.setRoles(role);
                    donationCenterPersonnelRepository.save(donationCenterPersonnel);
                    donationCenterService.save(dc);
                }
            }
        }
    }

    public void logoutDonor(Long donorId) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);
        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            donor.setPushToken(null);
            donor.setAppPlatform(null);
            donorRepository.save(donor);
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }

    }
}
