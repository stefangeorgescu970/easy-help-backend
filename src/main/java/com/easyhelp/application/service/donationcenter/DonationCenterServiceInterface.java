package com.easyhelp.application.service.donationcenter;

import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.locations.DonationCenter;

import java.util.List;

public interface DonationCenterServiceInterface {
    List<LocationDTO> getAll();

    void save(DonationCenter hospital);
}
