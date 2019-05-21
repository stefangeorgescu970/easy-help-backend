package com.easyhelp.application.controller;


import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationStatus;
import com.easyhelp.application.model.dto.account.RegisterDTO;
import com.easyhelp.application.model.dto.donation.DonationFormDTO;
import com.easyhelp.application.model.dto.donation.DonationSplitResultsDTO;
import com.easyhelp.application.model.dto.donation.DonationTestResultDTO;
import com.easyhelp.application.model.dto.requests.DonationRequestDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.model.misc.SsnData;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.requests.RequestUrgency;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.model.users.UserType;
import com.easyhelp.application.service.RegisterService;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donation.DonationServiceInterface;
import com.easyhelp.application.service.donation_booking.DonationBookingServiceInterface;
import com.easyhelp.application.service.donation_form.DonationFormServiceInterface;
import com.easyhelp.application.service.donation_request.DonationRequestServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.donationcenterpersonnel.DonationCenterPersonnelServiceInterface;
import com.easyhelp.application.service.donor.DonorServiceInterface;
import com.easyhelp.application.service.hospital.HospitalServiceInterface;
import com.easyhelp.application.service.patient.PatientServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.*;
import com.easyhelp.application.utils.response.Response;
import com.easyhelp.application.utils.response.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

@RestController
@RequestMapping("/mocks")
public class MockDataController {

    private final String girlSSN = "2941027370448";
    private final String boySSN = "1940205248591";

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DonationCenterPersonnelServiceInterface donationCenterPersonnelService;

    @Autowired
    private DoctorServiceInterface doctorService;

    @Autowired
    private HospitalServiceInterface hospitalService;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    @Autowired
    private DonationBookingServiceInterface donationBookingService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private DonorServiceInterface donorService;

    @Autowired
    private PatientServiceInterface patientService;

    @Autowired
    private BloodTypeServiceInterface bloodTypeService;

    @Autowired
    private DonationFormServiceInterface donationFormService;

    @Autowired
    private DonationServiceInterface donationService;

    @Autowired
    private DonationRequestServiceInterface donationRequestService;

    public MockDataController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @RequestMapping("/populateTables")
    private ResponseEntity<Response> populateMockDatabase() throws UserAlreadyRegisteredException, EntityNotFoundException, SsnInvalidException {

        deleteDataBase();


        addDonationCenters();
        addHospitals();

        addSysAdmin();
        addDoctors();
        addDonationCenterPersonnels();
        addDonors();
        addPatients();

        validateAccounts();

        addDonationForm(1L);
        addDonationForm(2L);
        addDonationForm(3L);

        addDonations();

        addDonationRequests();

        return ResponseBuilder.encode(HttpStatus.OK);
    }

    private void deleteDataBase() {
        try {
            Connection connection = DriverManager.getConnection(
                    System.getenv("JDBC_DATABASE_URL"),
                    System.getenv("JDBC_DATABASE_USERNAME"),
                    System.getenv("JDBC_DATABASE_PASSWORD")
            );

            Statement stm = connection.createStatement();
            String statement = "truncate table blood_type, doctor_roles, doctors, donation_bookings, donation_center_personnel_roles,\n" +
                    "donation_center_personnels, donation_centers, donation_forms, donation_requests, donation_test_results, donations,\n" +
                    "hospitals, patients, separated_blood_type, stored_bloods, donation_commitments, system_admin_roles, system_admins,\n" +
                    "donors, donor_roles restart identity;";
            stm.executeUpdate(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDonationCenters() {
        donationCenterService.save(new DonationCenter("CTS Alba", 23.5606313, 46.0793369, "B-dul Revoluţiei 1989 nr.23", County.ALBA));
        donationCenterService.save(new DonationCenter("CTS Alexandria", 25.3316741, 43.967557, "Str. Mihăiţă Filipescu nr. 12-14", County.TELEORMAN));
        donationCenterService.save(new DonationCenter("CTS Arad", 21.308712, 46.182262, "Str.Andrenyi Karoly nr. 2-4", County.ARAD));
        donationCenterService.save(new DonationCenter("CTS Bacau", 26.9, 46.56667, "Str. Mărăşeşti nr.22", County.BACAU));
        donationCenterService.save(new DonationCenter("CTS Baia Mare", 23.57949, 47.65331, "Str. G. Coşbuc nr.20A", County.MARAMURES));
        donationCenterService.save(new DonationCenter("CTS Barlad", 27.66667, 46.23333, "Str. M. Kogălniceanu nr.11", County.VASLUI));
        donationCenterService.save(new DonationCenter("CTS Bistrita", 24.50011, 47.13316, "Str. N. Bălcescu nr.11A", County.BISTRITA_NASAUD));
        donationCenterService.save(new DonationCenter("CTS Botosani", 26.66667, 47.75, "Str. Marchian nr.91", County.BOTOSANI));
        donationCenterService.save(new DonationCenter("CTS Brasov", 25.598963, 45.651402, "Str. V. Babeş nr.21", County.BRASOV));
        donationCenterService.save(new DonationCenter("CTS Braila", 27.969876, 45.269725, "Str. Radu S. Campiniu nr.25", County.BRAILA));
        donationCenterService.save(new DonationCenter("CTS Buzau", 26.83333, 45.15, "Str. G-ral Grigore Baştan nr.2", County.BUZAU));
        donationCenterService.save(new DonationCenter("CTS Calarasi", 27.3193336, 44.2063168, "Str. Bărăganului nr.1", County.CALARASI));
        donationCenterService.save(new DonationCenter("CTS Cluj", 0, 0, "Str. N. Bălcescu nr.18", County.CLUJ));
        donationCenterService.save(new DonationCenter("CTS Constanta", 0, 0, "Str. N. Iorga nr.85", County.CONSTANTA));
        donationCenterService.save(new DonationCenter("CTS Craiova", 0, 0, "Str. Tabaci nr.1", County.DOLJ));
        donationCenterService.save(new DonationCenter("CTS Targoviste", 0, 0, "Str. I.C.Brătianu nr.4", County.DAMBOVITA));
        donationCenterService.save(new DonationCenter("CTS Deva", 0, 0, "Str. 22 Decembrie, bl.D1, parter", County.HUNEDOARA));
        donationCenterService.save(new DonationCenter("CTS Hunedoara", 0, 0, "B-dul Victoriei 14", County.HUNEDOARA));
        donationCenterService.save(new DonationCenter("CTS Petrosani", 0, 0, "Str. 22 Decembrie nr.1", County.HUNEDOARA));

        donationCenterService.save(new DonationCenter("CTS Drobeta Turnu Severin", 0, 0, "Str. Carol I nr.16", County.MEHEDINTI));
        donationCenterService.save(new DonationCenter("CTS Focsani", 0, 0, "Str. Cuza Vodă nr.50-52", County.VRANCEA));
        donationCenterService.save(new DonationCenter("CTS Galati", 0, 0, "Str. Regiment 11 Siret nr. 48A", County.GALATI));
        donationCenterService.save(new DonationCenter("CTS Giurgiu", 0, 0, "Şos. Alexandriei nr.7-9", County.GIURGIU));
        donationCenterService.save(new DonationCenter("CTS Iasi", 0, 0, "Str. N. Bălcescu nr.21", County.IASI));
        donationCenterService.save(new DonationCenter("CTS Miercurea Ciuc", 0, 0, "Aleea Avântului nr.1", County.HARGHITA));
        donationCenterService.save(new DonationCenter("CTS Oradea", 0, 0, "Str. Louis Pasteur nr.30", County.BIHOR));
        donationCenterService.save(new DonationCenter("CTS Piatra Neamt", 0, 0, "Str. Traian nr.5", County.NEAMT));
        donationCenterService.save(new DonationCenter("CTS Pitesti", 0, 0, "Str. N. Vodă nr.43", County.ARGES));
        donationCenterService.save(new DonationCenter("CTS Campulung Muscel", 0, 0, "Str. Poenaru Bordea 12", County.ARGES));
        donationCenterService.save(new DonationCenter("CTS Ploiesti", 0, 0, "Şos. Vestului nr.24A", County.PRAHOVA));
        donationCenterService.save(new DonationCenter("CTS Resita", 0, 0, "Str. I.L. Caragiale nr.1", County.CARAS_SEVERIN));
        donationCenterService.save(new DonationCenter("CTS Ramnicu Valcea", 0, 0, "Str. G-ral Magheru nr.54", County.VALCEA));
        donationCenterService.save(new DonationCenter("CTS Satu Mare", 0, 0, "Str. Careului nr.26", County.SATU_MARE));
        donationCenterService.save(new DonationCenter("CTS Sfantu Gheorghe", 0, 0, "Str. Banki Donath nr.15A", County.COVASNA));
        donationCenterService.save(new DonationCenter("CTS Sibiu", 0, 0, "B-dul C.Coposu nr.2-4", County.SIBIU));
        donationCenterService.save(new DonationCenter("CTS Slatina", 0, 0, "Str. Ec. Theodoroiu nr.3", County.OLT));
        donationCenterService.save(new DonationCenter("CTS Slobozia", 0, 0, "Str. Decebal nr.1", County.IALOMITA));
        donationCenterService.save(new DonationCenter("CTS Suceava", 0, 0, "B-dul 1 Dec. 1918 nr.11", County.SUCEAVA));

        donationCenterService.save(new DonationCenter("CTS Targu Jiu", 0, 0, "Str. 22 Decembrie nr.22bis", County.GORJ));
        donationCenterService.save(new DonationCenter("CTS Targu Mures", 0, 0, "Str. Molter Karoly nr.2", County.MURES));
        donationCenterService.save(new DonationCenter("CTS Timisoara", 0, 0, "Str. Martir Marius Ciopec nr.1", County.TIMIS));
        donationCenterService.save(new DonationCenter("CTS Tulcea", 0, 0, "Str. Gloriei nr.22", County.TULCEA));
        donationCenterService.save(new DonationCenter("CTS Zalau", 0, 0, "Str. Simion Bărnuţiu nr.91", County.SALAJ));

        donationCenterService.save(new DonationCenter("CTS Bucuresti", 0, 0, "Str. C.Caracaş nr.2-8, sector 1", County.BUCURESTI));
        donationCenterService.save(new DonationCenter("Punct de recoltare Spitalul de Urgena Floreasca", 0, 0, "", County.BUCURESTI));
        donationCenterService.save(new DonationCenter("Punct de recoltare Institutul Clinic Fundeni", 0, 0, "", County.BUCURESTI));
        donationCenterService.save(new DonationCenter("Punct de recoltare Spitalul Militar", 0, 0, "", County.BUCURESTI));
        donationCenterService.save(new DonationCenter("Punct de recoltare Spitalul Universitar de Urgenta", 0, 0, "", County.BUCURESTI));

    }

    private void addHospitals() {
        hospitalService.save(new Hospital("Spital 1", 25.3316741, 43.967557, "Adresa 1", County.CLUJ));
        hospitalService.save(new Hospital("Spital 2", 23.57949, 47.65331, "Adresa 2", County.ARGES));
    }

    private void addDoctors() throws EntityNotFoundException, UserAlreadyRegisteredException, SsnInvalidException {
        registerService.registerUser(createDoctor("Andrei", "Cretu", County.BUCURESTI, "andrei@doc", boySSN, 1L));
        registerService.registerUser(createDoctor("Andra", "NU Moldovan", County.CLUJ, "andra@doc", girlSSN, 1L));
        registerService.registerUser(createDoctor("Alexandra", "Georgescu", County.DOLJ, "alex@doc", girlSSN, 1L));
        registerService.registerUser(createDoctor("Apopei", "Apostolescu", County.ARGES, "apopei@doc", girlSSN, 2L));

    }

    private RegisterDTO createDoctor(String fn, String ln, County county, String email, String ssn, Long locId) {
        RegisterDTO doctor = new RegisterDTO();
        doctor.setSkipSsnValidation(true);
        doctor.setFirstName(fn);
        doctor.setLastName(ln);
        doctor.setCounty(county);
        doctor.setEmail(email);
        doctor.setPassword(bCryptPasswordEncoder.encode("pass"));
        doctor.setSsn(ssn);
        doctor.setDateOfBirth(MiscUtils.getDataFromSsn(ssn).getDateOfBirth());
        doctor.setUserType(UserType.DOCTOR);
        doctor.setLocationId(locId);

        return doctor;
    }

    private void addDonationCenterPersonnels() throws EntityNotFoundException, UserAlreadyRegisteredException, SsnInvalidException {
        registerService.registerUser(createDCP("Mariana", "Vitalie", County.ALBA, "mariana@dcp", girlSSN, 1L));
        registerService.registerUser(createDCP("Mihai", "Vitalie", County.ALBA, "mihai@dcp", boySSN, 1L));
    }

    private RegisterDTO createDCP(String fn, String ln, County county, String email, String ssn, Long locId) {
        RegisterDTO dcp = new RegisterDTO();
        dcp.setSkipSsnValidation(true);
        dcp.setFirstName(fn);
        dcp.setLastName(ln);
        dcp.setCounty(county);
        dcp.setEmail(email);
        dcp.setPassword(bCryptPasswordEncoder.encode("pass"));
        dcp.setSsn(ssn);
        dcp.setDateOfBirth(MiscUtils.getDataFromSsn(ssn).getDateOfBirth());
        dcp.setUserType(UserType.DONATION_CENTER_PERSONNEL);
        dcp.setLocationId(locId);

        return dcp;
    }

    private void addSysAdmin() throws EntityNotFoundException, UserAlreadyRegisteredException, SsnInvalidException {
        registerService.registerUser(createSysAdmin("Admin", "Adminovici", County.CLUJ, "admin", boySSN));
    }

    private RegisterDTO createSysAdmin(String fn, String ln, County county, String email, String ssn) {
        RegisterDTO sysAdmin = new RegisterDTO();
        sysAdmin.setSkipSsnValidation(true);
        sysAdmin.setFirstName(fn);
        sysAdmin.setLastName(ln);
        sysAdmin.setCounty(county);
        sysAdmin.setEmail(email);
        sysAdmin.setPassword(bCryptPasswordEncoder.encode("admin"));
        sysAdmin.setSsn(ssn);
        sysAdmin.setDateOfBirth(MiscUtils.getDataFromSsn(ssn).getDateOfBirth());
        sysAdmin.setUserType(UserType.SYSADMIN);

        return sysAdmin;
    }

    private void validateAccounts() {
            try {
                for (long i = 1L; i <= 4L; i++) {
                    doctorService.reviewAccount(i, true);
                }

                for (long i = 1L; i <= 2L; i++) {
                    donationCenterPersonnelService.reviewAccount(i, true);
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
    }

    private void addDonors() throws EntityNotFoundException, UserAlreadyRegisteredException, SsnInvalidException {
        registerService.registerUser(createDonor("Razvan", "Dumitru", County.BRAILA, "razvan@don", boySSN));
        registerService.registerUser(createDonor("Daniel", "Dormutan", County.BUCURESTI, "daniel@don", boySSN));
        registerService.registerUser(createDonor("Miho", "Nevaman", County.BUCURESTI, "miho@don", boySSN));
        registerService.registerUser(createDonor("Sebastian", "Badita", County.BUCURESTI, "sebastian@don", boySSN));
        registerService.registerUser(createDonor("Sebi", "Puscatu", County.BUCURESTI, "drog@don", boySSN));
        registerService.registerUser(createDonor("Emin", "Eminovici", County.BUCURESTI, "emin@don", boySSN));
        registerService.registerUser(createDonor("Veronica", "Popescu", County.BUCURESTI, "vera@don", girlSSN));
        registerService.registerUser(createDonor("Andrei", "Adan", County.BUCURESTI, "andrei@don", boySSN));
        registerService.registerUser(createDonor("Test", "Donor", County.CLUJ, "test@don", girlSSN));
        registerService.registerUser(createDonor("Adrian", "Moldovan", County.ALBA, "adetz@don", girlSSN));
        registerService.registerUser(createDonor("Stefan", "Georgescu", County.ARGES, "stef@don", boySSN));

        donorService.updateBloodGroupOnDonor(1L, "A", false);
        donorService.updateBloodGroupOnDonor(2L, "AB", false);
        donorService.updateBloodGroupOnDonor(3L, "0", true);
        donorService.updateBloodGroupOnDonor(4L, "B", true);
        donorService.updateBloodGroupOnDonor(5L, "A", false);
    }

    private RegisterDTO createDonor(String fn, String ln, County county, String email, String ssn) {
        RegisterDTO donor = new RegisterDTO();
        donor.setSkipSsnValidation(true);
        donor.setFirstName(fn);
        donor.setLastName(ln);
        donor.setCounty(county);
        donor.setEmail(email);
        donor.setPassword(bCryptPasswordEncoder.encode("pass"));
        donor.setSsn(ssn);
        SsnData ssnData = MiscUtils.getDataFromSsn(ssn);
        donor.setDateOfBirth(ssnData.getDateOfBirth());
        donor.setUserType(UserType.DONOR);

        return donor;
    }

    private void addPatients() {
        try {
            patientService.addPatient(1L, "111111111", "A", false);
            patientService.addPatient(2L, "1111111111", "B", true);
            patientService.addPatient(2L, "21311111111", "AB", false);
            patientService.addPatient(3L, "22111111111", "0", true);
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    private void addDonationForm(Long forId) throws EntityNotFoundException {
        DonationFormDTO donationForm = new DonationFormDTO();

        donationForm.setDonorId(forId);

        donationForm.setGeneralGoodHealth(false);
        donationForm.setRecentLossOfWeight(false);
        donationForm.setRecentInexplicableFever(false);
        donationForm.setRecentStomatoTreatmentOrVaccine(false);
        donationForm.setCurrentDrugTreatment(false);
        donationForm.setSexWithHIVOrHepatitisLast12Months(false);
        donationForm.setSexWithPersonWhoInjectsDrugsLast12Months(false);
        donationForm.setSexWithProstituteLast12Months(false);
        donationForm.setSexWithMultiplePartnersLast12Months(false);
        donationForm.setInjectedDrugs(false);
        donationForm.setAcceptedMoneyOrDrugsForSex(false);
        donationForm.setChangedSexPartnerLast6Months(false);
        donationForm.setSurgeryOrInvestigationsLast12Months(false);
        donationForm.setTattoosOrPiercingsLast12Months(false);
        donationForm.setTransfusionLast12Months(false);
        donationForm.setBeenPregnant(false);
        donationForm.setBornLivedTraveledAbroad(false);
        donationForm.setPrisonLastYear(false);
        donationForm.setExposedHepatitis(false);
        donationForm.setSufferFromSet1(false);
        donationForm.setSufferFromSet2(false);
        donationForm.setSufferFromSet3(false);
        donationForm.setSufferFromSet4(false);
        donationForm.setSufferFromSet5(false);
        donationForm.setSufferFromSet6(false);
        donationForm.setSufferFromSet7(false);
        donationForm.setSmoker(false);
        donationForm.setBeenRefused(false);
        donationForm.setRequireAttentionPostDonation(false);

        donationForm.setNumberOfPartnersLast6Months(0);

        donationForm.setBirthDate("01 May 2019");
        donationForm.setLastMenstruation("01 May 2019");
        donationForm.setLastAlcoholUse("01 May 2019");

        donationForm.setTravelWhere("Capalna");
        donationForm.setTravelWhen("tommorow");
        donationForm.setAlcoholDrank("Jec");
        donationForm.setAlcoholQuantity("200");

        donorService.addDonationForm(donationForm);
    }

    private void addDonations() {
        donationService.saveDonation(buildWaitingTestResultDonation(1L, 1L, -1L));
        donationService.saveDonation(buildWaitingTestResultDonation(2L, 1L, 1L));
        donationService.saveDonation(buildWaitingTestResultDonation(3L, 1L, 1L));
        donationService.saveDonation(buildWaitingTestResultDonation(4L, 1L, -1L));
        donationService.saveDonation(buildWaitingTestResultDonation(5L, 1L, -1L));
        donationService.saveDonation(buildWaitingTestResultDonation(6L, 1L, -1L));
        donationService.saveDonation(buildWaitingTestResultDonation(7L, 1L, -1L));
        donationService.saveDonation(buildWaitingTestResultDonation(8L, 1L, -1L));

        DonationTestResultDTO donationTestResultDTO = new DonationTestResultDTO();


        try {
            donationTestResultDTO.setDonationId(1L);
            donationService.addTestResults(donationTestResultDTO);

            donationTestResultDTO.setDonationId(2L);
            donationService.addTestResults(donationTestResultDTO);

            donationTestResultDTO.setHiv(true);
            donationTestResultDTO.setHasFailed(true);

            donationTestResultDTO.setDonationId(3L);
            donationService.addTestResults(donationTestResultDTO);

            donationTestResultDTO.setDonationId(4L);
            donationService.addTestResults(donationTestResultDTO);

            DonationSplitResultsDTO donationSplitResultsDTO = new DonationSplitResultsDTO();
            donationSplitResultsDTO.setPlasmaUnits(1);
            donationSplitResultsDTO.setPlateletsUnits(2);
            donationSplitResultsDTO.setRedBloodCellsUnits(3);
            donationSplitResultsDTO.setDonationId(1L);
            donationService.separateBlood(donationSplitResultsDTO);

            donationSplitResultsDTO.setDonationId(2L);
            donationService.separateBlood(donationSplitResultsDTO);
        } catch (EasyHelpException e) {
            e.printStackTrace();
        }

    }

    private Donation buildWaitingTestResultDonation(Long donorId, Long donationCenterId, Long patientId) {
        try {
            Donor donor = donorService.findById(donorId);
            DonationCenter donationCenter = donationCenterService.findById(donationCenterId);

            Donation donation = new Donation();
            donation.setDonor(donor);
            donation.setDonationCenter(donationCenter);

            try {
                Patient patient = patientService.findById(patientId);
                donation.setPatient(patient);
                patient.getDonations().add(donation);
                patientService.save(patient);

                donation.setWithPatient(true);

            } catch (EntityNotFoundException e) {
                donation.setWithPatient(false);
            }

            donation.setDateAndTime(new Date());
            donation.setStatus(DonationStatus.AWAITING_CONTROL_TESTS);

            return donation;

        } catch (EasyHelpException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addDonationRequests() {
        DonationRequestDTO donationRequestDTO = new DonationRequestDTO();
        donationRequestDTO.setDoctorId(1L);
        donationRequestDTO.setPatientId(1L);
        donationRequestDTO.setBloodComponent(BloodComponent.PLASMA);
        donationRequestDTO.setQuantity(1.0);
        donationRequestDTO.setUrgency(RequestUrgency.MEDIUM);
        try {
            donationRequestService.requestDonation(donationRequestDTO);
            donationRequestDTO.setPatientId(2L);

            donationRequestService.requestDonation(donationRequestDTO);
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    private void addDonationCommitment() {

    }
}
