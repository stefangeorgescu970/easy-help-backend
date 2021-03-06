package com.easyhelp.application.service.donationcenter;

import com.easyhelp.application.model.dto.donor.outgoing.DonorDonationCenterDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.repository.DonationCenterRepository;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityCannotBeRemovedException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DonationCenterServiceImpl implements DonationCenterServiceInterface {

    @Autowired
    private DonationCenterRepository donationCenterRepository;

    @Override
    public List<DonationCenter> getAll() {
        return new ArrayList<>(donationCenterRepository.findAll());
    }

    @Override
    public DonationCenter save(DonationCenter donationCenter) {
        return donationCenterRepository.save(donationCenter);
    }

    @Override
    public void removeDonationCenter(Long donationCenterId) throws EntityCannotBeRemovedException, EntityNotFoundException {
        Optional<DonationCenter> donationCenter = donationCenterRepository.findById(donationCenterId);

        if (donationCenter.isPresent()) {
            DonationCenter donationCenterUnwrapped = donationCenter.get();

            if (!donationCenterUnwrapped.canBeRemoved()) {
                throw new EntityCannotBeRemovedException("There are other entities linked to this donation center, so it cannot be deleted!");
            }

            donationCenterRepository.deleteById(donationCenterId);
        } else {
            throw new EntityNotFoundException("Donation center with that id does not exist");
        }
    }

    @Override
    public DonationCenter findById(Long donationCenterId) throws EntityNotFoundException {
        Optional<DonationCenter> donationCenter = donationCenterRepository.findById(donationCenterId);

        if (donationCenter.isPresent()) {
            return donationCenter.get();
        } else {
            throw new EntityNotFoundException("Donation center with that id does not exist");
        }
    }

    @Override
    public List<DonationCenter> getDonationCentersInCounty(County county) {
        return donationCenterRepository
                .findAll()
                .stream()
                .filter(donationCenter -> donationCenter.getCounty() == county)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonorDonationCenterDTO> getOrderedDonationCenters(Double longitude, Double latitude) {
        if (longitude == null || latitude == null) {
            return donationCenterRepository.findAll().stream().map(dc -> new DonorDonationCenterDTO(dc, -1D)).collect(Collectors.toList());
        }
        return donationCenterRepository
                .findAll()
                .stream()
                .map(dc -> new DonorDonationCenterDTO(dc, MiscUtils.computeDistance(latitude, longitude, dc.getLatitude(), dc.getLongitude())))
                .sorted(Comparator.comparingDouble(DonorDonationCenterDTO::getDistance))
                .collect(Collectors.toList());
    }
}
