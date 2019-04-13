package com.easyhelp.application.service.donor;

import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;

import java.util.Calendar;
import java.util.List;

public interface DonorServiceInterface {
    void updateCountyOnDonor(Long donorId, County newCounty) throws EntityNotFoundException;

    void updateSsnOnDonor(Long donorId, String newSsn) throws EntityNotFoundException, SsnInvalidException;

    void updateBloodGroupOnDonor(Long donorId, String groupLetter, Boolean rh) throws EntityNotFoundException;

    void bookDonationHour(Long donorId, Calendar selectedHour, Long donationCenterId) throws EntityNotFoundException;

    List<Donor> getDonorsInCounty(County county);

    DonorSummary getDonorSummary(Long donorId) throws EntityNotFoundException;
}
