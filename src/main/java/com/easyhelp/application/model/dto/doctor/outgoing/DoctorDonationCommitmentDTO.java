package com.easyhelp.application.model.dto.doctor.outgoing;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import com.easyhelp.application.model.dto.misc.outgoing.SeparatedBloodTypeDTO;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationCommitmentStatus;
import lombok.Data;

@Data
public class DoctorDonationCommitmentDTO extends BaseOutgoingDTO {
    private Long id;
    private DonationCommitmentStatus status;
    private SeparatedBloodTypeDTO separatedBloodType;
    private ExtendedOutgoingLocationDTO donationCenter;

    public DoctorDonationCommitmentDTO(DonationCommitment donationCommitment) {
        id = donationCommitment.getId();
        status = donationCommitment.getStatus();
        separatedBloodType = new SeparatedBloodTypeDTO(donationCommitment.getStoredBlood().getSeparatedBloodType());
        donationCenter = new ExtendedOutgoingLocationDTO(donationCommitment.getDonationCenter());
    }
}
