package com.easyhelp.application.service.donation_booking;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.dto.booking.AvailableDate;
import com.easyhelp.application.model.dto.booking.DonationBookingDTO;
import com.easyhelp.application.repository.DonationBookingRepository;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
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
    public List<AvailableDate> getAvailableDates(Long donorId, Date selectedHour, Long donationCenterId) {
        final DateFormat df = DateFormat.getDateTimeInstance();
        List<AvailableDate> allHours = MiscUtils.getAllHoursForWeek(selectedHour);

        List<Date> bookedDates = donationBookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getDonationCenter().getId().equals(donationCenterId))
                .filter(d -> d.getDateAndTime().after(selectedHour))
                .map(DonationBooking::getDateAndTime)
                .collect(Collectors.toList());

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
