package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.blood.StoredBloodDTO;
import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationCommitmentStatus;
import lombok.Data;

@Data
public class DonationCommitmentDTO extends BaseDTO {

    private Long id;
    private LocationDTO donationCenter;
    private StoredBloodDTO storedBlood;
    private DonationCommitmentStatus status;

    public DonationCommitmentDTO(DonationCommitment donationCommitment) {
        id = donationCommitment.getId();
        donationCenter = new LocationDTO(donationCommitment.getDonationCenter());
        storedBlood = new StoredBloodDTO(donationCommitment.getStoredBlood());
        status = donationCommitment.getStatus();
    }
}
