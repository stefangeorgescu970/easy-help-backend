package com.easyhelp.application.service.donationcenter;

import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.utils.exceptions.EntityCannotBeRemovedException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DonationCenterServiceInterface {
    List<LocationDTO> getAll();

    void save(DonationCenter donationCenter);

    void removeDonationCenter(Long donationCenterId) throws EntityCannotBeRemovedException, EntityNotFoundException;

    DonationCenter findById(Long donationCenterId) throws EntityNotFoundException;
}
