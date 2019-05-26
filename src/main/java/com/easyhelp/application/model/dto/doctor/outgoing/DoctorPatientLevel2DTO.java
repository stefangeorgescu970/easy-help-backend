package com.easyhelp.application.model.dto.doctor.outgoing;

import com.easyhelp.application.model.dto.doctor.outgoing.DoctorPatientLevel1DTO;
import com.easyhelp.application.model.dto.misc.outgoing.BloodTypeDTO;
import com.easyhelp.application.model.requests.Patient;
import lombok.Data;

@Data
public class DoctorPatientLevel2DTO extends DoctorPatientLevel1DTO {
    private BloodTypeDTO bloodType;

    public DoctorPatientLevel2DTO(Patient patient) {
        super(patient);
        bloodType = new BloodTypeDTO(patient.getBloodType());
    }
}

