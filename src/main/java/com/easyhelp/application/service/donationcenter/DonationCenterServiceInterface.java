package com.easyhelp.application.service.donationcenter;

import com.easyhelp.application.model.dto.donor.outgoing.DonorDonationCenterDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.utils.exceptions.EntityCannotBeRemovedException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DonationCenterServiceInterface {
    List<DonationCenter> getAll();

    DonationCenter save(DonationCenter donationCenter);

    void removeDonationCenter(Long donationCenterId) throws EntityCannotBeRemovedException, EntityNotFoundException;

    DonationCenter findById(Long donationCenterId) throws EntityNotFoundException;

    List<DonationCenter> getDonationCentersInCounty(County county);

    List<DonorDonationCenterDTO> getOrderedDonationCenters(Double longitude, Double latitude);
}
