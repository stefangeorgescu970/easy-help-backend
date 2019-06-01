package com.easyhelp.application.model.dto.dcp.outgoing;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.BloodTypeDTO;
import com.easyhelp.application.model.users.Donor;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class DCPDonorAccountDTO extends BaseOutgoingDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Long id;
    private Boolean canDonate;
    private BloodTypeDTO bloodType;
    private Boolean isMale;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    public DCPDonorAccountDTO(Donor donor) {
        firstName = donor.getFirstName();
        lastName = donor.getLastName();
        dateOfBirth = donor.getDateOfBirth();
        email = donor.getEmail();
        id = donor.getId();
        isMale = donor.getIsMale();

        BloodType bloodTypeExt = donor.getBloodType();
        if (bloodTypeExt != null) {
           this.bloodType = new BloodTypeDTO(bloodTypeExt);
        }

        canDonate = donor.canDonate();
    }
}
