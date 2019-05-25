package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.booking.DonationBookingDTO;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class DonorSummaryDTO extends BaseDTO {
    private Integer donationsNumber;
    private DonationDTO lastDonation;
    private DonationBookingDTO nextBooking;
    private Date streakBegin;

    public DonorSummaryDTO(DonorSummary donorSummary) {
        donationsNumber = donorSummary.getDonationsNumber();
        if (donorSummary.getNextBooking() != null)
            nextBooking = new DonationBookingDTO(donorSummary.getNextBooking());
        if (donorSummary.getLastDonation() != null)
            lastDonation = new DonationDTO(donorSummary.getLastDonation());
        if (donorSummary.getDonationStreakBegin() != null)
            streakBegin = donorSummary.getDonationStreakBegin();
    }
}
