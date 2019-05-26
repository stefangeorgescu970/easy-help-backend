package com.easyhelp.application.model.dto.dcp.outgoing;

import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.BaseOutgoingLocationDTO;
import lombok.Data;

import java.util.Date;

@Data
public class DCPDonationDTO extends BaseOutgoingDTO {
    private Long id;
    private DCPDonorAccountDTO donor;
    private BaseOutgoingLocationDTO donationCenter;
    private Date date;

    public DCPDonationDTO(Donation donation) {
        id = donation.getId();
        donationCenter = new BaseOutgoingLocationDTO(donation.getDonationCenter());
        donor = new DCPDonorAccountDTO(donation.getDonor());
        date = donation.getDateAndTime();
    }
}
