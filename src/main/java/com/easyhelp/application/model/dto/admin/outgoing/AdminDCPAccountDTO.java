package com.easyhelp.application.model.dto.admin;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AdminDCPAccountDTO extends BaseOutgoingDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private County county;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String locationName;
    private String locationPhone;

    public AdminDCPAccountDTO(DonationCenterPersonnel dcp) {
        id = dcp.getId();
        firstName = dcp.getFirstName();
        lastName = dcp.getLastName();
        email = dcp.getEmail();
        county = dcp.getCounty();
        dateOfBirth = dcp.getDateOfBirth();
        locationName = dcp.getDonationCenter().getName();
        locationPhone = dcp.getDonationCenter().getPhone();
    }
}
