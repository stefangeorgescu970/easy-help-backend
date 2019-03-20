package com.easyhelp.application.model.dto.accountrequest;

import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.users.DonationCenterPersonnel;

public class DonationCenterPersonnelAccountRequestDTO extends AccountRequestDTO {
    private DonationCenter donationCenter;

    public DonationCenterPersonnelAccountRequestDTO(DonationCenterPersonnel dcp) {
        super(dcp);
        donationCenter = dcp.getDonationCenter();
    }
}
