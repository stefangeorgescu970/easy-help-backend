package com.easyhelp.application.service.donationcenter;

import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.repository.DonationCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationCenterServiceImpl implements DonationCenterServiceInterface {

    @Autowired
    DonationCenterRepository donationCenterRepository;

    @Override
    public List<LocationDTO> getAll() {
        return donationCenterRepository.findAll().stream().map(LocationDTO::new).collect(Collectors.toList());
    }

    @Override
    public void save(DonationCenter donationCenter) {
        donationCenterRepository.save(donationCenter);
    }
}
