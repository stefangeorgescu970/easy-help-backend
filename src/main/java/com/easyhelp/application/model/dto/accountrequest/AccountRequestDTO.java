package com.easyhelp.application.model.dto.accountrequest;

import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.users.PartnerUser;
import com.easyhelp.application.model.users.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AccountRequestDTO extends BaseDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private String country;
    private UserType userType;

    public AccountRequestDTO(PartnerUser user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        city = user.getCity();
        country = user.getCountry();
        userType = user.getUserType();
    }
}
