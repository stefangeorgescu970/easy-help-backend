package com.easyhelp.application.model.dto.donor.outgoing;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class DonorDonationBookingDTO extends BaseOutgoingDTO {
    private Long id;
    private ExtendedOutgoingLocationDTO donationCenter;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime bookingDate;

    public DonorDonationBookingDTO(DonationBooking booking) {
        this.id = booking.getId();
        this.bookingDate = booking.getDateAndTime();
        this.donationCenter = new ExtendedOutgoingLocationDTO(booking.getDonationCenter());
    }
}
