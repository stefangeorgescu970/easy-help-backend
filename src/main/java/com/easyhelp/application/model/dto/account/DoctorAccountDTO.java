package com.easyhelp.application.model.dto.account;


import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.users.Doctor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class /**/DoctorAccountDTO extends AccountDTO {
    private LocationDTO hospital;

    public DoctorAccountDTO(Doctor doctor) {
        super(doctor);
        if (doctor.getHospital() != null) {
            hospital = new LocationDTO(doctor.getHospital());
        }
    }

}
