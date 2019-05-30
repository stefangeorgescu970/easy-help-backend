package com.easyhelp.application.model.dto.donor.outgoing;

import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.donor.outgoing.DonorDonationBookingDTO;
import com.easyhelp.application.model.dto.donor.outgoing.DonorDonationDTO;
import lombok.Data;

import java.util.Date;

@Data
public class DonorSummaryDTO extends BaseOutgoingDTO {
    private Integer donationsNumber;
    private DonorDonationDTO lastDonation;
    private DonorDonationBookingDTO nextBooking;
    private Date streakBegin;
    private Integer numberOfPatientsYouCouldHelp;

    public DonorSummaryDTO(DonorSummary donorSummary) {
        donationsNumber = donorSummary.getDonationsNumber();
        if (donorSummary.getNextBooking() != null)
            nextBooking = new DonorDonationBookingDTO(donorSummary.getNextBooking());
        if (donorSummary.getLastDonation() != null)
            lastDonation = new DonorDonationDTO(donorSummary.getLastDonation());
        if (donorSummary.getDonationStreakBegin() != null)
            streakBegin = donorSummary.getDonationStreakBegin();
        if (donorSummary.getNumberOfPatientsYouCouldHelp() != null) {
            numberOfPatientsYouCouldHelp = donorSummary.getNumberOfPatientsYouCouldHelp();
        }
    }
}
