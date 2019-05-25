package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.donations.DonationForm;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.donation.DonationFormDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.users.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AccountDTO extends BaseDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private County county;
    private UserType userType;
    private String ssn;

    private String bloodGroupLetter;
    private Boolean rh;

    private Long locationId;

    private Boolean isMale;
    private DonationFormDTO donationForm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    public AccountDTO(ApplicationUser user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        county = user.getCounty();
        userType = user.getUserType();
        ssn = user.getSsn();
        dateOfBirth = user.getDateOfBirth();

        if (user instanceof Donor) {
            Donor donor = (Donor) user;
            BloodType bloodType = donor.getBloodType();
            if (bloodType != null) {
                bloodGroupLetter = bloodType.getGroupLetter();
                rh = bloodType.getRh();
            }

            isMale = donor.getIsMale();
            if (donor.getDonationForm() != null) {
                donationForm = new DonationFormDTO(donor.getDonationForm());
            }
        }

        if (user instanceof DonationCenterPersonnel) {
            DonationCenterPersonnel donationCenterPersonnel = (DonationCenterPersonnel) user;
            locationId = donationCenterPersonnel.getDonationCenter().getId();
        }

        if (user instanceof Doctor) {
            Doctor doctor = (Doctor) user;
            locationId = doctor.getHospital().getId();
        }
    }
}
