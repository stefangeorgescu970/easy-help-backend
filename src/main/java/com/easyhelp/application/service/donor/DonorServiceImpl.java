package com.easyhelp.application.service.donor;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.DonorRepository;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DonorServiceImpl implements DonorServiceInterface {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private BloodTypeServiceInterface bloodTypeService;

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
}
