package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.users.ApplicationUser;
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
    private String city;
    private UserType userType;
    private String ssn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    public AccountDTO(ApplicationUser user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        city = user.getCity();
        userType = user.getUserType();
        ssn = user.getSsn();
        dateOfBirth = user.getDateOfBirth();
    }
}
