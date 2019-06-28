package com.easyhelp.application.model.dto.misc.outgoing;

import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.dto.dcp.outgoing.DCPDonorAccountDTO;
import lombok.Data;

@Data
public class StoredBloodLevel2DTO extends StoredBloodLevel1DTO {
    private DCPDonorAccountDTO donor;

    public StoredBloodLevel2DTO(StoredBlood storedBlood) {
        super(storedBlood);
        donor = new DCPDonorAccountDTO(storedBlood.getDonor());
    }
}
