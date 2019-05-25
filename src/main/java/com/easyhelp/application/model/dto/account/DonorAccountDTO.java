package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.users.Donor;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DonorAccountDTO extends BaseDTO {
    String firstName;
    String lastName;
    String email;
    Long id;
    Boolean canDonate;
    Boolean rh;
    String group;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date dateOfBirth;

    public DonorAccountDTO(Donor donor) {
        firstName = donor.getFirstName();
        lastName = donor.getLastName();
        dateOfBirth = donor.getDateOfBirth();
        email = donor.getEmail();
        id = donor.getId();

        BloodType bloodType = donor.getBloodType();
        if (bloodType != null) {
            rh = bloodType.getRh();
            group = bloodType.getGroupLetter();
        }

        canDonate = donor.canDonate();
    }
}
