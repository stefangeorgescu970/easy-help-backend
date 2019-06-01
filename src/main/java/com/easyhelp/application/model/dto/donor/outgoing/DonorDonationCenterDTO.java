package com.easyhelp.application.model.dto.donor.outgoing;

import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import lombok.Data;

@Data
public class DonorDonationCenterDTO extends ExtendedOutgoingLocationDTO {
    private Double distance;

    public DonorDonationCenterDTO(DonationCenter donationCenter, Double distance) {
        super(donationCenter);
        this.distance = distance;
    }
}
