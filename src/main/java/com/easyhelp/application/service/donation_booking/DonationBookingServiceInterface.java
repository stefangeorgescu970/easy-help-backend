package com.easyhelp.application.service.donation_booking;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.dto.booking.AvailableDate;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.Date;
import java.util.List;

public interface DonationBookingServiceInterface {

    void save(DonationBooking donationBooking);

    List<AvailableDate> getAvailableDates(Long donorId, Date selectedHour, Long donationCenterId);

    List<DonationBooking> getDCBookings(Long donationCenterId);

    DonationBooking getDonorBooking(Long donorId) throws EntityNotFoundException;
}
