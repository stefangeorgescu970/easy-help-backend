package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

@Data
public class DonorSummaryDTO extends BaseDTO {
    private Integer donationsNumber;
    private String lastDonation;
    private String nextBooking;

    public DonorSummaryDTO(DonorSummary donorSummary) {
        donationsNumber = donorSummary.getDonationsNumber();
        lastDonation = donorSummary.getLastDonation();
        nextBooking = donorSummary.getNextBooking();
    }
}
