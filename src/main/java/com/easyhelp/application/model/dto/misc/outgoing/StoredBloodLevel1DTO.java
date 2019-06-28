package com.easyhelp.application.model.dto.misc.outgoing;

import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StoredBloodLevel1DTO extends BaseOutgoingDTO {
    private Long id;
    private SeparatedBloodTypeDTO separatedBloodType;
    private Double amount;
    private String bagIdentifier;
    private Integer daysUntilExpired;

    public StoredBloodLevel1DTO(StoredBlood storedBlood) {
        id = storedBlood.getId();
        separatedBloodType = new SeparatedBloodTypeDTO(storedBlood.getSeparatedBloodType());
        amount = storedBlood.getAmount();
        bagIdentifier = storedBlood.getBagIdentifier();
        daysUntilExpired = storedBlood.daysUntilExpiry();
    }
}
