package com.easyhelp.application.model.dto.account;


import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.model.users.Doctor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoctorAccountRequestDTO extends AccountRequestDTO {
    private Hospital hospital;

    public DoctorAccountRequestDTO(Doctor doctor) {
        super(doctor);
        hospital = doctor.getHospital();
    }

}
