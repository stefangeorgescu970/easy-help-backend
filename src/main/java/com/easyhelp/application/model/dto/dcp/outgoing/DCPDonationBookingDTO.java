package com.easyhelp.application.model.dto.dcp.outgoing;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.donor.outgoing.DonationFormDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class DCPDonationBookingDTO extends BaseOutgoingDTO {

    private Long id;
    private DCPDonorAccountDTO donor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime bookingDate;
    private DonationFormDTO donationForm;

    public DCPDonationBookingDTO(DonationBooking booking) {
        this.id = booking.getId();
        this.donor = new DCPDonorAccountDTO(booking.getDonor());
        this.bookingDate = booking.getDateAndTime();
        if (booking.getDonor().getDonationForm() != null) {
            donationForm = new DonationFormDTO(booking.getDonor().getDonationForm());
        }
    }
}
