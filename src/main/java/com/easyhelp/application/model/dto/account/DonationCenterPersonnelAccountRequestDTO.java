package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import lombok.Data;

@Data
public class DonationCenterPersonnelAccountRequestDTO extends AccountRequestDTO {
    private DonationCenter donationCenter;

    public DonationCenterPersonnelAccountRequestDTO(DonationCenterPersonnel dcp) {
        super(dcp);
        donationCenter = dcp.getDonationCenter();
    }
}
