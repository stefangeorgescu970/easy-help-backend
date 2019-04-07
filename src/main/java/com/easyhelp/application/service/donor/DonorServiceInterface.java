package com.easyhelp.application.service.donor;

import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;

import java.util.Date;
import java.util.List;

public interface DonorServiceInterface {
    void updateCountyOnDonor(Long donorId, County newCounty) throws EntityNotFoundException;

    void updateSsnOnDonor(Long donorId, String newSsn) throws EntityNotFoundException, SsnInvalidException;

    void updateBloodGroupOnDonor(Long donorId, String groupLetter, Boolean rh) throws EntityNotFoundException;

//    List<Date> getAvailableHours(Date selectedDate, Long donationCenterId);

    void bookDonationHour(Long donorId, Date selectedHour, Long donationCenterId) throws EntityNotFoundException;
}
