package com.easyhelp.application.model.dto.dcp.outgoing;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DCPDonationBookingDTO extends BaseOutgoingDTO {

    private Long id;
    private DCPDonorAccountDTO donor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date bookingDate;

    public DCPDonationBookingDTO(DonationBooking booking) {
        this.id = booking.getId();
        this.donor = new DCPDonorAccountDTO(booking.getDonor());
        this.bookingDate = booking.getDateAndTime();
    }
}
