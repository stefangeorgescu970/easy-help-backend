package com.easyhelp.application.service.donation_booking;

import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.DonationStatus;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.DonationBookingRepository;
import com.easyhelp.application.service.donation.DonationServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.donor.DonorServiceInterface;
import com.easyhelp.application.service.patient.PatientServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.PushNotificationUtils;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.PushTokenUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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

    @Autowired
    private DonationServiceInterface donationService;

    @Autowired
    private PatientServiceInterface patientService;

    @Override
    public DonationBooking save(DonationBooking donationBooking) {
        return donationBookingRepository.save(donationBooking);
    }

    @Override
    public DonationBooking findById(Long donationBookingId) throws EntityNotFoundException {
        Optional<DonationBooking> donationBookingOptional = donationBookingRepository.findById(donationBookingId);
        if (!donationBookingOptional.isPresent()) {
            throw new EntityNotFoundException("Donation Booking with that id does not exist");
        }

        return donationBookingOptional.get();
    }

    @Override
    public List<AvailableDate> getAvailableBookingSlots(Long donationCenterId) throws EntityNotFoundException {
        ZonedDateTime currentDate = ZonedDateTime.now();
        List<AvailableDate> allHours = MiscUtils.getAllHoursForWeek();

        AvailableDate today = allHours.get(0);
        today.setAvailableHours(today.getAvailableHours().stream().filter(zonedDateTime -> zonedDateTime.isAfter(currentDate)).collect(Collectors.toList()));

        DonationCenter donationCenter = donationCenterService.findById(donationCenterId);

        List<ZonedDateTime> bookedDates = donationBookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getDonationCenter().getId().equals(donationCenterId))
                .filter(b -> b.getDateAndTime().isAfter(currentDate))
                .map(DonationBooking::getDateAndTime)
                .collect(Collectors.toList());

        for (AvailableDate date : allHours) {
            Map<ZonedDateTime, Long> counterMap = bookedDates.stream().collect(Collectors.groupingBy(d -> d, Collectors.counting()));
            Set<ZonedDateTime> unavailableSlots = bookedDates
                    .stream()
                    .filter(b -> counterMap.get(b) >= donationCenter.getNumberOfConcurrentDonors())
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
                .sorted((b1, b2) -> b2.getDateAndTime().compareTo(b1.getDateAndTime()))
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
    public Long getDonorsNumberForSlot(Long donationCenterId, LocalDateTime slotSelected) {

        return donationBookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getDonationCenter().getId().equals(donationCenterId))
                .filter(b -> b.getDateAndTime().equals(slotSelected))
                .count();
    }

    @Override
    public void cancelBooking(Long bookingId, Boolean shouldNotifyDonor) throws EntityNotFoundException {
        Optional<DonationBooking> donationBookingOptional = donationBookingRepository.findById(bookingId);

        if (!donationBookingOptional.isPresent())
            throw new EntityNotFoundException("No donation booking with that id exists");

        DonationBooking donationBooking = donationBookingOptional.get();
        Donor donor = donationBooking.getDonor();
        DonationCenter donationCenter = donationBooking.getDonationCenter();

        donor.setDonationBooking(null);
        donationCenter.getDonationBookings().remove(donationBooking);

        if (shouldNotifyDonor) {
            try {
                PushNotificationUtils.sendPushNotification(donor, "Your recent booking was cancelled by the donation center.");
            } catch (PushTokenUnavailableException ignored) {
            }
        }

        donationCenterService.save(donationCenter);
        donorService.save(donor);
        donationBookingRepository.delete(donationBooking);
    }

    @Override
    public Donation createDonationFromBooking(Long bookingId, String bloodGroup, Boolean rh) throws EntityNotFoundException {
        Optional<DonationBooking> donationBookingOptional = donationBookingRepository.findById(bookingId);

        if (!donationBookingOptional.isPresent())
            throw new EntityNotFoundException("No donation booking with that id exists");

        DonationBooking donationBooking = donationBookingOptional.get();
        Donor donor = donorService.findByEmail(donationBooking.getDonor().getEmail());
        if (donor.getBloodType() == null || !donor.getBloodType().getGroupLetter().equals(bloodGroup) || donor.getBloodType().getRh() != rh) {
            donorService.updateBloodGroupOnDonor(donor.getId(), bloodGroup, rh);
        }

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setDonationCenter(donationBooking.getDonationCenter());
        donation.setStatus(DonationStatus.AWAITING_CONTROL_TESTS);
        donation.setDate(LocalDate.now());

        if (donationBooking.getIsForPatient()) {
            donation.setPatient(donationBooking.getPatient());
            donation.setWithPatient(true);
            donationBooking.getPatient().getDonations().add(donation);
            patientService.save(donation.getPatient());
        }

        donationBookingRepository.delete(donationBooking);
        return donationService.saveDonation(donation);
    }
}
