package com.easyhelp.application.model.dto.booking;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.account.AccountDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DonationBookingDTO extends BaseDTO {

    private Long id;
    private AccountDTO donor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date bookingDate;

    public DonationBookingDTO(DonationBooking booking) {
        this.id = booking.getId();
        this.donor = new AccountDTO(booking.getDonor());
        this.bookingDate = booking.getDateAndTime();
    }

}
