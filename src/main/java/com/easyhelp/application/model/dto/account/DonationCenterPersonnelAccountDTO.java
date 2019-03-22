package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import lombok.Data;

@Data
public class DonationCenterPersonnelAccountDTO extends AccountDTO {
    private LocationDTO donationCenter;

    public DonationCenterPersonnelAccountDTO(DonationCenterPersonnel dcp) {
        super(dcp);
        if (dcp.getDonationCenter() != null) {
            donationCenter = new LocationDTO(dcp.getDonationCenter());
        }
    }
}
