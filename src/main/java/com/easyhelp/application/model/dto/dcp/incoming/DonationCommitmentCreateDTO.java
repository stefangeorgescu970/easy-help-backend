package com.easyhelp.application.model.dto.dcp.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import lombok.Data;

@Data
public class DonationCommitmentCreateDTO extends BaseIncomingDTO {
    private Long donationCenterId;
    private Long storedBloodId;
    private Long donationRequestId;
}
