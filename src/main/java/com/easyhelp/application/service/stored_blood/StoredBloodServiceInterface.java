package com.easyhelp.application.service.stored_blood;

import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.dto.misc.outgoing.BloodStockDTO;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface StoredBloodServiceInterface {
    StoredBlood storeBlood(StoredBlood storedBlood);

    StoredBlood findById(Long storedBloodId) throws EntityNotFoundException;

    List<StoredBlood> getAvailableBloodInDC(Long donationCenterId) throws EntityNotFoundException;

    void removeBlood(Long storedBloodId) throws EntityNotFoundException;

    List<StoredBlood> getExpiredBloodInDC(Long donationCenterId);

    List<BloodStockDTO> getBloodStocksInCountry();

    List<BloodStockDTO> getBloodStocksInDC(Long donationCenterId);
}
