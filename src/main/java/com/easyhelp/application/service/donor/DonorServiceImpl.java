package com.easyhelp.application.service.donor;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.DonorRepository;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.service.donation_booking.DonationBookingServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DonorServiceImpl implements DonorServiceInterface {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonationBookingServiceInterface donationBookingService;

    @Autowired
    private BloodTypeServiceInterface bloodTypeService;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    @Override
    public void updateCountyOnDonor(Long donorId, County newCounty) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            donor.setCounty(newCounty);
            donorRepository.save(donor);
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }
    }

    @Override
    public void updateSsnOnDonor(Long donorId, String newSsn) throws EntityNotFoundException, SsnInvalidException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            Date dob = MiscUtils.getDateFromSsn(newSsn);
            donor.setSsn(newSsn);
            donor.setDateOfBirth(dob);
            donorRepository.save(donor);
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }
    }

    @Override
    public void updateBloodGroupOnDonor(Long donorId, String groupLetter, Boolean rh) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            BloodType bloodTypeInDB = bloodTypeService.findBloodTypeInDB(groupLetter, rh);
            if (bloodTypeInDB == null) {
                BloodType newBloodType = new BloodType(groupLetter, rh);
                Set<Donor> donorSet = new HashSet<>();
                donorSet.add(donor);
                newBloodType.setDonors(donorSet);
                donor.setBloodType(newBloodType);
                bloodTypeService.saveBloodType(newBloodType);
            } else {
                donor.setBloodType(bloodTypeInDB);
                bloodTypeInDB.getDonors().add(donor);
                bloodTypeService.saveBloodType(bloodTypeInDB);
            }
            donorRepository.save(donor);
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }
    }


    @Override
    public void bookDonationHour(Long donorId, Date selectedHour, Long donationCenterId) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);

        //check if donor has already made a booking

        //TODO - also throw error for not found donation center

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            DonationCenter donationCenter = donationCenterService.findById(donationCenterId);
            DonationBooking booking = new DonationBooking();
            booking.setDateAndTime(selectedHour);
            booking.setDonor(donor);
            booking.setDonationCenter(donationCenter);
            donor.setDonationBooking(booking);
            donationBookingService.save(booking);
            donorRepository.save(donor);
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }

    }

    @Override
    public List<Donor> getDonorsInCounty(County county) {
        return donorRepository.findAllByCounty(county);
    }

    @Override
    public DonorSummary getDonorSummary(Long donorId) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);
        DonorSummary donorSummary = new DonorSummary();

        //check if donor has already made a booking

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            donorSummary.setDonationsNumber(donor.getDonations().size());
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }

        return donorSummary;
    }
}
