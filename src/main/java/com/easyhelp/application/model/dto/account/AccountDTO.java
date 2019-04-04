package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.users.ApplicationUser;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.model.users.PartnerUser;
import com.easyhelp.application.model.users.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
        }
    }
}
