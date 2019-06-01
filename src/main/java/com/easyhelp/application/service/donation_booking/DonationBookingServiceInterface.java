package com.easyhelp.application.service.donation_booking;

import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface DonationBookingServiceInterface {

    DonationBooking save(DonationBooking donationBooking);

    DonationBooking findById(Long donationBookingId) throws EntityNotFoundException;

    List<AvailableDate> getAvailableBookingSlots(Long donationCenterId) throws EntityNotFoundException;

    List<DonationBooking> getDCBookings(Long donationCenterId);

    DonationBooking getDonorBooking(Long donorId) throws EntityNotFoundException;

    Long getDonorsNumberForSlot(Long donationCenterId, LocalDateTime slotSelected);

    void cancelBooking(Long bookingId, Boolean shouldNotifyDonor) throws EntityNotFoundException;

    Donation createDonationFromBooking(Long bookingId, String bloodGroup, Boolean rh) throws EntityNotFoundException;
}
