package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.account.DonorAccountDTO;
import com.easyhelp.application.model.dto.location.LocationDTO;
import lombok.Data;

import java.util.Date;

@Data
public class DonationDTO extends BaseDTO {
    private Long id;
    private DonorAccountDTO donorAccountDTO;
    private LocationDTO donationCenter;
    private DonationTestResultDTO donationTestResultDTO;
    private Date date;

    public DonationDTO(Donation donation) {
        id = donation.getId();
        donationCenter = new LocationDTO(donation.getDonationCenter());
        if(donation.getTestResults() != null) {
            donationTestResultDTO = new DonationTestResultDTO(donation.getTestResults());
        }
        donorAccountDTO = new DonorAccountDTO(donation.getDonor());
        date = donation.getDateAndTime();
    }
}
