package com.easyhelp.application.model.dto.donor.outgoing;

import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DonorDonationDTO extends BaseOutgoingDTO {
    private Long id;
    private ExtendedOutgoingLocationDTO donationCenter;
    private DonorDonationTestResultsDTO donationTestResultDTO;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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
