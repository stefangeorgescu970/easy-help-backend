package com.easyhelp.application.model.dto.dcp.outgoing;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.users.Donor;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DCPDonorAccountDTO extends BaseOutgoingDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Long id;
    private Boolean canDonate;
    private Boolean rh;
    private String group;
    private Boolean isMale;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    public DCPDonorAccountDTO(Donor donor) {
        firstName = donor.getFirstName();
        lastName = donor.getLastName();
        dateOfBirth = donor.getDateOfBirth();
        email = donor.getEmail();
        id = donor.getId();
        isMale = donor.getIsMale();

        BloodType bloodType = donor.getBloodType();
        if (bloodType != null) {
            rh = bloodType.getRh();
            group = bloodType.getGroupLetter();
        }

        canDonate = donor.canDonate();
    }
}