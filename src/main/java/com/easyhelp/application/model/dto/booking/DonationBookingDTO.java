package com.easyhelp.application.model.dto.booking;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.account.AccountDTO;
import lombok.Data;

import java.util.Date;

@Data
public class DonationBookingDTO extends BaseDTO {

    private Long id;
    private AccountDTO donor;
    private Date bookingDate;

    public DonationBookingDTO(DonationBooking booking) {
        this.id = booking.getId();
        this.donor = new AccountDTO(booking.getDonor());
        this.bookingDate = booking.getDateAndTime();
    }

}
