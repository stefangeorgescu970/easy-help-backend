package com.easyhelp.application.service.donor;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.dto.donor.incoming.DonationFormCreateDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.users.AppPlatform;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface DonorServiceInterface {
    void updateCountyOnDonor(Long donorId, County newCounty) throws EntityNotFoundException;

    void updateSsnOnDonor(Long donorId, String newSsn) throws EntityNotFoundException, SsnInvalidException;

    void updateBloodGroupOnDonor(Long donorId, String groupLetter, Boolean rh) throws EntityNotFoundException;

    DonationBooking bookDonationHour(Long donorId, ZonedDateTime selectedHour, Long donationCenterId, String patientSSN) throws EntityNotFoundException, EntityAlreadyExistsException;

    List<Donor> getDonorsInCounty(County county);

    List<Donor> filterDonors(County county, String groupLetter, Boolean canDonate);

    DonorSummary getDonorSummary(Long donorId) throws EntityNotFoundException;

    void addDonationForm(DonationFormCreateDTO donationForm) throws EntityNotFoundException;

    Donor save(Donor donor);

    Donor findByEmail(String email);

    void registerPushToken(Long donorId, String token, AppPlatform appPlatform) throws EntityNotFoundException;

    Donor findDonorByEmail(String email) throws EntityNotFoundException;

    Donor findById(Long donorId) throws EntityNotFoundException;
}
