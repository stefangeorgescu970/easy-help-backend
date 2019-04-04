package com.easyhelp.application.service.donor;

import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;

public interface DonorServiceInterface {
    void updateCountyOnDonor(Long donorId, County newCounty) throws EntityNotFoundException;

    void updateSsnOnDonor(Long donorId, String newSsn) throws EntityNotFoundException, SsnInvalidException;

    void updateBloodGroupOnDonor(Long donorId, String groupLetter, Boolean rh) throws EntityNotFoundException;
}
