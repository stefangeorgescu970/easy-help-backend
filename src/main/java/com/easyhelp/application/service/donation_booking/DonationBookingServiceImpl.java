package com.easyhelp.application.service.donation_booking;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.repository.DonationBookingRepository;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceImpl;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DonationBookingServiceImpl implements DonationBookingServiceInterface {

    @Autowired
    private DonationBookingRepository donationBookingRepository;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

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
    public Long getDonorsNumberForSlot(Long donationCenterId, Date slotSelected) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        String s = dateFormat.format(slotSelected);
        return donationBookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getDonationCenter().getId().equals(donationCenterId))
                .filter(b -> dateFormat.format(b.getDateAndTime()).equals(s))
                .count();
    }
}
