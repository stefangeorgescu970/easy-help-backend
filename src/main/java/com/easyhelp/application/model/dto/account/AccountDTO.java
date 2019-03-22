package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.users.PartnerUser;
import com.easyhelp.application.model.users.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO extends BaseDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private String country;
    private UserType userType;

    public AccountDTO(PartnerUser user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        city = user.getCity();
        country = user.getCountry();
        userType = user.getUserType();
    }
}
