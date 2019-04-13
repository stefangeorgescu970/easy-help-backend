package com.easyhelp.application.service.donation_booking;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.repository.DonationBookingRepository;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationBookingServiceImpl implements DonationBookingServiceInterface {

    @Autowired
    private DonationBookingRepository donationBookingRepository;

    @Override
    public void save(DonationBooking donationBooking) {
        donationBookingRepository.save(donationBooking);
    }

    @Override
    public List<AvailableDate> getAvailableBookingSlots(Long donationCenterId) {
        Date currentDate = new Date();
        List<AvailableDate> allHours = MiscUtils.getAllHoursForWeek(currentDate);

        List<Date> bookedDates = donationBookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getDonationCenter().getId().equals(donationCenterId))
                .filter(d -> d.getDateAndTime().after(currentDate))
                .map(DonationBooking::getDateAndTime)
                .collect(Collectors.toList());

        //TODO - check if multiple dates can be added to the hash set (write own hash func if not)
        //TODO - check if donation center has number of booked slots for a certain date and hour < concurrentDonations property of that dc.

        for (AvailableDate date : allHours) {
            date.getAvailableHours().removeAll(bookedDates);
        }

        return allHours;
    }

    @Override
    public List<DonationBooking> getDCBookings(Long donationCenterId) {
        return donationBookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getDonationCenter().getId().equals(donationCenterId))
                .collect(Collectors.toList());
    }

    @Override
    public DonationBooking getDonorBooking(Long donorId) throws EntityNotFoundException {
        List<DonationBooking> donationBooking = donationBookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getDonor().getId().equals(donorId))
                .collect(Collectors.toList());
        if (donationBooking.isEmpty())
            throw new EntityNotFoundException("No current donation");
        return donationBooking.get(0);
    }
}
