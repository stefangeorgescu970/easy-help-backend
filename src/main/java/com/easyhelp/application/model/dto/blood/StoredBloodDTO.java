package com.easyhelp.application.model.dto.blood;

import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.account.DonorAccountDTO;
import com.easyhelp.application.model.dto.location.LocationDTO;
import lombok.Data;

@Data
public class StoredBloodDTO extends BaseDTO {
    private Long id;
    private DonorAccountDTO donorAccountDTO;
    private LocationDTO donationCenter;
    private SeparatedBloodTypeDTO separatedBloodTypeDTO;
    private Double amount;

    public StoredBloodDTO(StoredBlood storedBlood) {
        id = storedBlood.getId();
        donorAccountDTO = new DonorAccountDTO(storedBlood.getDonor());
        donationCenter = new LocationDTO(storedBlood.getDonationCenter());
        separatedBloodTypeDTO = new SeparatedBloodTypeDTO(storedBlood.getSeparatedBloodType());
        amount = storedBlood.getAmount();
    }
}
