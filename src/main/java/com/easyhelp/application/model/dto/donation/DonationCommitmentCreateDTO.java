package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

@Data
public class DonationCommitmentCreateDTO extends BaseDTO {
    private Long donationCenterId;
    private Long storedBloodId;
    private Long donationRequestId;
}
