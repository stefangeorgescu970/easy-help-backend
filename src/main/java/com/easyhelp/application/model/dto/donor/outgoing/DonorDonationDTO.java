package com.easyhelp.application.model.dto.donor.outgoing;

import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import lombok.Data;

import java.util.Date;

@Data
public class DonorDonationDTO extends BaseOutgoingDTO {
    private Long id;
    private ExtendedOutgoingLocationDTO donationCenter;
    private DonorDonationTestResultsDTO donationTestResultDTO;
    private Date date;

    public DonorDonationDTO(Donation donation) {
        id = donation.getId();
        donationCenter = new ExtendedOutgoingLocationDTO(donation.getDonationCenter());
        if (donation.getTestResults() != null) {
            donationTestResultDTO = new DonorDonationTestResultsDTO(donation.getTestResults());
        }
        date = donation.getDateAndTime();
    }
}
