package com.easyhelp.application.service.donor;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.DonationForm;
import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.dto.donor.incoming.DonationFormCreateDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.misc.SsnData;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.users.AppPlatform;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.DonorRepository;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.service.donation_booking.DonationBookingServiceInterface;
import com.easyhelp.application.service.donation_form.DonationFormServiceInterface;
import com.easyhelp.application.service.donation_request.DonationRequestServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.patient.PatientServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class DonorServiceImpl implements DonorServiceInterface {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonationBookingServiceInterface donationBookingService;

    @Autowired
    private BloodTypeServiceInterface bloodTypeService;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    @Autowired
    private DonationFormServiceInterface donationFormService;

    @Autowired
    private PatientServiceInterface patientService;

    @Autowired
    private DonationRequestServiceInterface donationRequestService;

    @Override
    public void updateCountyOnDonor(Long donorId, County newCounty) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            donor.setCounty(newCounty);
            donorRepository.save(donor);
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }
    }

    @Override
    public void updateSsnOnDonor(Long donorId, String newSsn) throws EntityNotFoundException, SsnInvalidException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);
        MiscUtils.validateSsn(newSsn);

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            SsnData ssnData = MiscUtils.getDataFromSsn(newSsn);
            LocalDate dob = ssnData.getDateOfBirth();
            donor.setIsMale(ssnData.getIsMale());
            donor.setSsn(newSsn);
            donor.setDateOfBirth(dob);
            donorRepository.save(donor);
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }
    }

    @Override
    public void updateBloodGroupOnDonor(Long donorId, String groupLetter, Boolean rh) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            BloodType bloodTypeInDB = bloodTypeService.findBloodTypeInDB(groupLetter, rh);
            if (bloodTypeInDB == null) {
                BloodType newBloodType = new BloodType(groupLetter, rh);
                Set<Donor> donorSet = new HashSet<>();
                donorSet.add(donor);
                newBloodType.setDonors(donorSet);
                donor.setBloodType(newBloodType);
                bloodTypeService.saveBloodType(newBloodType);
            } else {
                donor.setBloodType(bloodTypeInDB);
                bloodTypeInDB.getDonors().add(donor);
                bloodTypeService.saveBloodType(bloodTypeInDB);
            }
            donorRepository.save(donor);
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }
    }


    @Override
    public DonationBooking bookDonationHour(Long donorId, ZonedDateTime selectedHour, Long donationCenterId, String patientSSN) throws EntityNotFoundException, EntityAlreadyExistsException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);
        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            if (donor.getDonationBooking() != null)
                throw new EntityAlreadyExistsException("The donor has already made a booking");

            DonationCenter donationCenter = donationCenterService.findById(donationCenterId);
            if (donationBookingService.getDonorsNumberForSlot(donationCenterId, selectedHour.toLocalDateTime()) >= donationCenter.getNumberOfConcurrentDonors())
                throw new EntityAlreadyExistsException("There are too many booking requests for this slot");

            DonationBooking booking = new DonationBooking();
            Patient patient = null;

            if (patientSSN != null) {
                patient = patientService.findBySSN(patientSSN);
                if (patient == null) {
                    // Backup, you should never put ssn on call if the ssn was not previously checked

                    throw new EntityNotFoundException("No patient was found with provided ssn.");
                }
                booking.setPatient(patient);
                booking.setIsForPatient(true);
                if (patient.getDonationBookings() == null) {
                    Set<DonationBooking> donationBookings = new HashSet<>();
                    donationBookings.add(booking);
                    patient.setDonationBookings(donationBookings);
                } else {
                    patient.getDonationBookings().add(booking);
                }
            } else {
                booking.setIsForPatient(false);
            }

            booking.setDateAndTime(selectedHour);
            booking.setDonor(donor);
            booking.setDonationCenter(donationCenter);
            donor.setDonationBooking(booking);
            donationCenter.addBooking(booking);
            donationBookingService.save(booking);
            donorRepository.save(donor);
            donationCenterService.save(donationCenter);

            if (patient != null) {
                patientService.save(patient);
            }

            return booking;
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }

    }

    @Override
    public List<Donor> getDonorsInCounty(County county) {
        return donorRepository.findAllByCounty(county);
    }

    @Override
    public DonorSummary getDonorSummary(Long donorId) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);
        DonorSummary donorSummary = new DonorSummary();

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            donorSummary.setDonationsNumber(donor.getDonations().size());

            if (donor.getDonationForm() != null) {
                donorSummary.setDonationForm(donor.getDonationForm());
            }

            if (donor.getDonationBooking() != null &&
                    donor.getDonationBooking().getDateAndTime().isAfter(ZonedDateTime.now()))
                donorSummary.setNextBooking(donor.getDonationBooking());

            if (!donor.getDonations().isEmpty()) {
                Optional<Donation> lastDonation = donor.getDonations().stream().max(Comparator.comparing(Donation::getDate));
                lastDonation.ifPresent(donorSummary::setLastDonation);

                LocalDate today = LocalDate.now();
                List<Donation> lastYearDonations = donor.getDonations().stream().filter(donation -> {
                    long days = DAYS.between(donation.getDate(), today);
                    return days < 365;
                }).collect(Collectors.toList());

                int maxNumber = donor.getIsMale() ? 5 : 4;

                if (lastYearDonations.size() >= maxNumber) {
                    lastYearDonations = lastYearDonations.stream().sorted(Comparator.comparing(Donation::getDate)).limit(maxNumber).collect(Collectors.toList());
                    donorSummary.setDonationStreakBegin(lastYearDonations.get(0).getDate());
                }
            }

            if (donor.getBloodType() != null) {
                donorSummary.setNumberOfPatientsYouCouldHelp(donationRequestService.getDonationRequestsDonorCouldDonateFor(donor).size());
            }
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }

        return donorSummary;
    }

    @Override
    public void addDonationForm(DonationFormCreateDTO donationFormCreateDTO) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donationFormCreateDTO.getDonorId());
        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            DonationForm donationForm = new DonationForm();
            donationForm.setGeneralGoodHealth(donationFormCreateDTO.getGeneralGoodHealth());
            donationForm.setRecentLossOfWeight(donationFormCreateDTO.getRecentLossOfWeight());
            donationForm.setRecentInexplicableFever(donationFormCreateDTO.getRecentInexplicableFever());
            donationForm.setRecentStomatoTreatmentOrVaccine(donationFormCreateDTO.getRecentStomatoTreatmentOrVaccine());
            donationForm.setCurrentDrugTreatment(donationFormCreateDTO.getCurrentDrugTreatment());
            donationForm.setSexWithHIVOrHepatitisLast12Months(donationFormCreateDTO.getSexWithHIVOrHepatitisLast12Months());
            donationForm.setSexWithPersonWhoInjectsDrugsLast12Months(donationFormCreateDTO.getSexWithPersonWhoInjectsDrugsLast12Months());
            donationForm.setSexWithProstituteLast12Months(donationFormCreateDTO.getSexWithProstituteLast12Months());
            donationForm.setSexWithMultiplePartnersLast12Months(donationFormCreateDTO.getSexWithMultiplePartnersLast12Months());
            donationForm.setInjectedDrugs(donationFormCreateDTO.getInjectedDrugs());
            donationForm.setAcceptedMoneyOrDrugsForSex(donationFormCreateDTO.getAcceptedMoneyOrDrugsForSex());
            donationForm.setChangedSexPartnerLast6Months(donationFormCreateDTO.getChangedSexPartnerLast6Months());
            donationForm.setNumberOfPartnersLast6Months(donationFormCreateDTO.getNumberOfPartnersLast6Months());
            donationForm.setSurgeryOrInvestigationsLast12Months(donationFormCreateDTO.getSurgeryOrInvestigationsLast12Months());
            donationForm.setTattoosOrPiercingsLast12Months(donationFormCreateDTO.getTattoosOrPiercingsLast12Months());
            donationForm.setTransfusionLast12Months(donationFormCreateDTO.getTransfusionLast12Months());
            donationForm.setBeenPregnant(donationFormCreateDTO.getBeenPregnant());
            donationForm.setBirthDate(donationFormCreateDTO.getBirthDate());
            donationForm.setLastMenstruation(donationFormCreateDTO.getLastMenstruation());
            donationForm.setBornLivedTraveledAbroad(donationFormCreateDTO.getBornLivedTraveledAbroad());
            donationForm.setTravelWhere(donationFormCreateDTO.getTravelWhere());
            donationForm.setTravelWhen(donationFormCreateDTO.getTravelWhen());
            donationForm.setPrisonLastYear(donationFormCreateDTO.getPrisonLastYear());
            donationForm.setExposedHepatitis(donationFormCreateDTO.getExposedHepatitis());
            donationForm.setSufferFromSet1(donationFormCreateDTO.getSufferFromSet1());
            donationForm.setSufferFromSet2(donationFormCreateDTO.getSufferFromSet2());
            donationForm.setSufferFromSet3(donationFormCreateDTO.getSufferFromSet3());
            donationForm.setSufferFromSet4(donationFormCreateDTO.getSufferFromSet4());
            donationForm.setSufferFromSet5(donationFormCreateDTO.getSufferFromSet5());
            donationForm.setSufferFromSet6(donationFormCreateDTO.getSufferFromSet6());
            donationForm.setSufferFromSet7(donationFormCreateDTO.getSufferFromSet7());
            donationForm.setSmoker(donationFormCreateDTO.getSmoker());
            donationForm.setLastAlcoholUse(donationFormCreateDTO.getLastAlcoholUse());
            donationForm.setAlcoholDrank(donationFormCreateDTO.getAlcoholDrank());
            donationForm.setAlcoholQuantity(donationFormCreateDTO.getAlcoholQuantity());
            donationForm.setBeenRefused(donationFormCreateDTO.getBeenRefused());
            donationForm.setRequireAttentionPostDonation(donationFormCreateDTO.getRequireAttentionPostDonation());

            DonationForm oldForm = donationFormService.getDonationFormForDonor(donor.getId());
            if (oldForm != null)
                donationFormService.removeForm(oldForm);

            donationForm.setDonor(donor);
            donor.setDonationForm(donationForm);
            donationFormService.addDonationForm(donationForm);
            donorRepository.save(donor);
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }
    }

    @Override
    public Donor save(Donor donor) {
        return donorRepository.save(donor);
    }

    @Override
    public void registerPushToken(Long donorId, String token, AppPlatform appPlatform) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            donor.setPushToken(token);
            donor.setAppPlatform(appPlatform);
            donorRepository.save(donor);
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }
    }

    @Override
    public Donor findDonorByEmail(String email) throws EntityNotFoundException {
        Donor donor = donorRepository.findByEmail(email);

        if (donor != null) {
            return donor;
        } else {
            throw new EntityNotFoundException("No donor was found with provided email.");
        }
    }

    @Override
    public Donor findById(Long donorId) throws EntityNotFoundException {
        Optional<Donor> donor = donorRepository.findById(donorId);

        if (donor.isPresent()) {
            return donor.get();
        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }
    }

    @Override
    public List<Donor> filterDonors(County county, String groupLetter, Boolean canDonate) {
        if (groupLetter == null)
            return donorRepository.findAllByCounty(county)
                    .stream()
                    .filter(d -> d.canDonate() == canDonate)
                    .collect(Collectors.toList());

        return donorRepository.findAllByCounty(county)
                .stream()
                .filter(d -> d.canDonate() == canDonate && d.getBloodType() != null && d.getBloodType().getGroupLetter().equals(groupLetter))
                .collect(Collectors.toList());
    }

    @Override
    public Donor findByEmail(String email) {
        return donorRepository.findByEmail(email);
    }
}
