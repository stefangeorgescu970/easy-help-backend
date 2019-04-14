package com.easyhelp.application.service.donation_booking;

import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DonationBookingServiceInterface {

    void save(DonationBooking donationBooking);

    List<AvailableDate> getAvailableBookingSlots(Long donationCenterId) throws EntityNotFoundException;

    List<DonationBooking> getDCBookings(Long donationCenterId);

    DonationBooking getDonorBooking(Long donorId) throws EntityNotFoundException;

    void cancelBooking(Long bookingId) throws EntityNotFoundException;
}
