package com.easyhelp.application.service.donor;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.DonationForm;
import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.dto.donation.DonationFormDTO;
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
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.patient.PatientServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    public void updateSsnOnDonor(Long donorId, String newSsn, Boolean skipCheck) throws EntityNotFoundException, SsnInvalidException {
        Optional<Donor> donorOptional = donorRepository.findById(donorId);
        if (!skipCheck) {
            // Possibility to skip check for ssn. Make sure at least the first 7 digits are ok, since these
            // tell the parser the dob and sex of a donor.

            MiscUtils.validateSsn(newSsn);
        }

        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            SsnData ssnData = MiscUtils.getDataFromSsn(newSsn);
            Date dob = ssnData.getDateOfBirth();
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
    public void bookDonationHour(Long donorId, Date selectedHour, Long donationCenterId, String patientSSN) throws EntityNotFoundException, EntityAlreadyExistsException {
        Date date = selectedHour;
        date.setHours(date.getHours() + date.getTimezoneOffset() / 60);
        Optional<Donor> donorOptional = donorRepository.findById(donorId);
        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            if (donor.getDonationBooking() != null)
                throw new EntityAlreadyExistsException("The donor has already made a booking");

            DonationCenter donationCenter = donationCenterService.findById(donationCenterId);
            if (donationBookingService.getDonorsNumberForSlot(donationCenterId, date) >= donationCenter.getNumberOfConcurrentDonors())
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
            }

            booking.setDateAndTime(date);
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

            if (donor.getDonationBooking() != null &&
                    donor.getDonationBooking().getDateAndTime().after(new Date()))
                donorSummary.setNextBooking(donor.getDonationBooking());

            if (!donor.getDonations().isEmpty()) {
                Optional<Donation> lastDonation = donor.getDonations().stream().max(Comparator.comparing(Donation::getDateAndTime));
                lastDonation.ifPresent(donorSummary::setLastDonation);
            }

        } else {
            throw new EntityNotFoundException("No donor was found with provided id.");
        }

        return donorSummary;
    }

    @Override
    public void addDonationForm(DonationFormDTO donationFormDTO) throws EntityNotFoundException {
        Optional<Donor> donorOptional = donorRepository.findById(donationFormDTO.getDonorId());
        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            DonationForm donationForm = new DonationForm();
            donationForm.setGeneralGoodHealth(donationFormDTO.getGeneralGoodHealth());
            donationForm.setRecentLossOfWeight(donationFormDTO.getRecentLossOfWeight());
            donationForm.setRecentInexplicableFever(donationFormDTO.getRecentInexplicableFever());
            donationForm.setRecentStomatoTreatmentOrVaccine(donationFormDTO.getRecentStomatoTreatmentOrVaccine());
            donationForm.setCurrentDrugTreatment(donationFormDTO.getCurrentDrugTreatment());
            donationForm.setSexWithHIVOrHepatitisLast12Months(donationFormDTO.getSexWithHIVOrHepatitisLast12Months());
            donationForm.setSexWithPersonWhoInjectsDrugsLast12Months(donationFormDTO.getSexWithPersonWhoInjectsDrugsLast12Months());
            donationForm.setSexWithProstituteLast12Months(donationFormDTO.getSexWithProstituteLast12Months());
            donationForm.setSexWithMultiplePartnersLast12Months(donationFormDTO.getSexWithMultiplePartnersLast12Months());
            donationForm.setInjectedDrugs(donationFormDTO.getInjectedDrugs());
            donationForm.setAcceptedMoneyOrDrugsForSex(donationFormDTO.getAcceptedMoneyOrDrugsForSex());
            donationForm.setChangedSexPartnerLast6Months(donationFormDTO.getChangedSexPartnerLast6Months());
            donationForm.setNumberOfPartnersLast6Months(donationFormDTO.getNumberOfPartnersLast6Months());
            donationForm.setSurgeryOrInvestigationsLast12Months(donationFormDTO.getSurgeryOrInvestigationsLast12Months());
            donationForm.setTattoosOrPiercingsLast12Months(donationFormDTO.getTattoosOrPiercingsLast12Months());
            donationForm.setTransfusionLast12Months(donationFormDTO.getTransfusionLast12Months());
            donationForm.setBeenPregnant(donationFormDTO.getBeenPregnant());
            donationForm.setBirthDate(donationFormDTO.getBirthDate());
            donationForm.setLastMenstruation(donationFormDTO.getLastMenstruation());
            donationForm.setBornLivedTraveledAbroad(donationFormDTO.getBornLivedTraveledAbroad());
            donationForm.setTravelWhere(donationFormDTO.getTravelWhere());
            donationForm.setTravelWhen(donationFormDTO.getTravelWhen());
            donationForm.setPrisonLastYear(donationFormDTO.getPrisonLastYear());
            donationForm.setSufferFromSet1(donationFormDTO.getSufferFromSet1());
            donationForm.setSufferFromSet2(donationFormDTO.getSufferFromSet2());
            donationForm.setSufferFromSet3(donationFormDTO.getSufferFromSet3());
            donationForm.setSufferFromSet4(donationFormDTO.getSufferFromSet4());
            donationForm.setSufferFromSet5(donationFormDTO.getSufferFromSet5());
            donationForm.setSufferFromSet6(donationFormDTO.getSufferFromSet6());
            donationForm.setSufferFromSet7(donationFormDTO.getSufferFromSet7());
            donationForm.setSmoker(donationFormDTO.getSmoker());
            donationForm.setLastAlcoholUse(donationFormDTO.getLastAlcoholUse());
            donationForm.setAlcoholDrank(donationFormDTO.getAlcoholDrank());
            donationForm.setAlcoholQuantity(donationFormDTO.getAlcoholQuantity());
            donationForm.setBeenRefused(donationFormDTO.getBeenRefused());
            donationForm.setRequireAttentionPostDonation(donationFormDTO.getRequireAttentionPostDonation());

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
    public void save(Donor donor) {
        donorRepository.save(donor);
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
