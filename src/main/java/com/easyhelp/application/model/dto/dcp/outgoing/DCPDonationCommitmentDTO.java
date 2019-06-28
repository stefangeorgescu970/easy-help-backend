package com.easyhelp.application.model.dto.dcp.outgoing;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import com.easyhelp.application.model.dto.misc.outgoing.StoredBloodLevel1DTO;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationCommitmentStatus;
import com.easyhelp.application.model.requests.RequestUrgency;
import lombok.Data;

@Data
public class DCPDonationCommitmentDTO extends BaseOutgoingDTO {

    private Long id;
    private StoredBloodLevel1DTO storedBlood;
    private DonationCommitmentStatus status;
    private RequestUrgency urgency;
    private ExtendedOutgoingLocationDTO destinationHospital;

    public DCPDonationCommitmentDTO(DonationCommitment donationCommitment) {
        id = donationCommitment.getId();
        storedBlood = new StoredBloodLevel1DTO(donationCommitment.getStoredBlood());
        status = donationCommitment.getStatus();
        urgency = donationCommitment.getDonationRequest().getUrgency();
        destinationHospital = new ExtendedOutgoingLocationDTO(donationCommitment.getDonationRequest().getDoctor().getHospital());
    }
}
