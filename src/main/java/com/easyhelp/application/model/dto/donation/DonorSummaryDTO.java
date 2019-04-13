package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.booking.DonationBookingDTO;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class DonorSummaryDTO extends BaseDTO {
    private Integer donationsNumber;
    //TODO change this to donation DTO
    private String lastDonation;
    private DonationBookingDTO nextBooking;

    public DonorSummaryDTO(DonorSummary donorSummary) {
        donationsNumber = donorSummary.getDonationsNumber();
        nextBooking = new DonationBookingDTO(donorSummary.getNextBooking());
    }
}
