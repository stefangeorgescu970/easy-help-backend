package com.easyhelp.application.model.dto.admin.outgoing;

import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import lombok.Data;

@Data
public class AdminDonationCenterDTO extends ExtendedOutgoingLocationDTO {
    private Integer numberOfConcurrentDonors;

    public AdminDonationCenterDTO(DonationCenter donationCenter){
        super(donationCenter);
        this.numberOfConcurrentDonors = donationCenter.getNumberOfConcurrentDonors();
    }
}
