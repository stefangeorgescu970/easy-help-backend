package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.location.LocationDTO;
import lombok.Data;

import java.util.Date;

@Data
public class DonationDTO extends BaseDTO {
    private LocationDTO donationCenter;
    private DonationTestResultDTO donationTestResultDTO;
    private Date date;

    public DonationDTO(Donation donation) {
        donationCenter = new LocationDTO(donation.getDonationCenter());
        donationTestResultDTO = new DonationTestResultDTO(donation.getTestResults());
        date = donation.getDateAndTime();
    }
}
