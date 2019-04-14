package com.easyhelp.application.service.donation_booking;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.DonationBookingRepository;
import com.easyhelp.application.repository.DonorRepository;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceImpl;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.donor.DonorServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DonationBookingServiceImpl implements DonationBookingServiceInterface {

    @Autowired
    private DonationBookingRepository donationBookingRepository;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    @Autowired
    private DonorServiceInterface donorService;

    @Override
    public void save(DonationBooking donationBooking) {
        donationBookingRepository.save(donationBooking);
    }

    @Override
    public List<AvailableDate> getAvailableBookingSlots(Long donationCenterId) throws EntityNotFoundException {
        Date currentDate = new Date();
        List<AvailableDate> allHours = MiscUtils.getAllHoursForWeek(currentDate);

        DonationCenter donationCenter = donationCenterService.findById(donationCenterId);

        List<Date> bookedDates = donationBookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getDonationCenter().getId().equals(donationCenterId))
                .filter(b -> b.getDateAndTime().after(currentDate))
                .map(DonationBooking::getDateAndTime)
                .collect(Collectors.toList());

        for (AvailableDate date : allHours) {
            Map<Date, Long> counterMap = bookedDates.stream().collect(Collectors.groupingBy(d -> d, Collectors.counting()));
            Set<Date> unavailableSlots = bookedDates
                    .stream()
                    .filter(b -> counterMap.get(b) > donationCenter.getNumberOfConcurrentDonors())
                    .collect(Collectors.toSet());

            date.getAvailableHours().removeAll(unavailableSlots);
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

    @Override
    public void cancelBooking(Long bookingId) throws EntityNotFoundException {
        Optional<DonationBooking> donationBookingOptional = donationBookingRepository.findById(bookingId);

        if (!donationBookingOptional.isPresent())
            throw new EntityNotFoundException("No donation booking with that id exists");

        DonationBooking donationBooking = donationBookingOptional.get();
        Donor donor = donationBooking.getDonor();
        DonationCenter donationCenter = donationBooking.getDonationCenter();

        donor.setDonationBooking(null);
        donationCenter.getDonationBookings().remove(donationBooking);

        donationCenterService.save(donationCenter);
        donorService.save(donor);
        donationBookingRepository.delete(donationBooking);
    }
}
