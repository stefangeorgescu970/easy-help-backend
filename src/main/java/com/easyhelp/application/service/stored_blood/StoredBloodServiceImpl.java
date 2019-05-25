package com.easyhelp.application.service.stored_blood;

import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.repository.StoredBloodRepository;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoredBloodServiceImpl implements StoredBloodServiceInterface {

    @Autowired
    private StoredBloodRepository storedBloodRepository;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    @Override
    public void storeBlood(StoredBlood storedBlood) {
        storedBloodRepository.save(storedBlood);
    }

    @Override
    public StoredBlood findById(Long storedBloodId) throws EntityNotFoundException {
        Optional<StoredBlood> storedBloodOptional = storedBloodRepository.findById(storedBloodId);

        if (storedBloodOptional.isPresent()) {
            return storedBloodOptional.get();
        }

        throw new EntityNotFoundException("Stored blood with that id is not in the db");
    }

    @Override
    public List<StoredBlood> getAvailableBloodInDC(Long donationCenterId) throws EntityNotFoundException {
        DonationCenter donationCenter = donationCenterService.findById(donationCenterId);

        return donationCenter.getStoredBloodSet().stream()
                .filter(storedBlood -> storedBlood.getDonationCommitment() == null)
                .filter(StoredBlood::getIsUsable)
                .collect(Collectors.toList());
    }

    @Override
    public void removeBlood(StoredBlood storedBlood) {
        storedBlood.getDonationCenter().getStoredBloodSet().remove(storedBlood);
        storedBlood.getDonor().getStoredBloodSet().remove(storedBlood);
        storedBlood.getSeparatedBloodType().getStoredBloodSet().remove(storedBlood);

        storedBloodRepository.delete(storedBlood);
    }
}
