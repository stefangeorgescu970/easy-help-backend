package com.easyhelp.application.model.dto.doctor.outgoing;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import com.easyhelp.application.model.dto.misc.outgoing.SeparatedBloodTypeDTO;
import com.easyhelp.application.model.dto.misc.outgoing.StoredBloodLevel1DTO;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationCommitmentStatus;
import lombok.Data;

@Data
public class DoctorDonationCommitmentDTO extends BaseOutgoingDTO {
    private Long id;
    private DonationCommitmentStatus status;
    private StoredBloodLevel1DTO storedBlood;
    private ExtendedOutgoingLocationDTO donationCenter;

    public DoctorDonationCommitmentDTO(DonationCommitment donationCommitment) {
        id = donationCommitment.getId();
        status = donationCommitment.getStatus();
        storedBlood = new StoredBloodLevel1DTO(donationCommitment.getStoredBlood());
        donationCenter = new ExtendedOutgoingLocationDTO(donationCommitment.getDonationCenter());
    }
}
