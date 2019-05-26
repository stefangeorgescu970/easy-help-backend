package com.easyhelp.application.model.dto.admin;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.users.Doctor;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AdminDoctorAccountDTO extends BaseOutgoingDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private County county;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String locationName;
    private String locationPhone;

    public AdminDoctorAccountDTO(Doctor doctor) {
        id = doctor.getId();
        firstName = doctor.getFirstName();
        lastName = doctor.getLastName();
        email = doctor.getEmail();
        county = doctor.getCounty();
        dateOfBirth = doctor.getDateOfBirth();
        locationName = doctor.getHospital().getName();
        locationPhone = doctor.getHospital().getPhone();
    }
}
