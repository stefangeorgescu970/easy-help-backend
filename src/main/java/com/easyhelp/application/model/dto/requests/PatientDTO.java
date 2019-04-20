package com.easyhelp.application.model.dto.requests;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.blood.BloodTypeDTO;
import com.easyhelp.application.model.requests.Patient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PatientDTO extends BaseDTO {

    // maybe add name later
    private String ssn;
    private BloodTypeDTO bloodType;
    private Long doctorId;
    private Long id;

    public PatientDTO(Patient patient) {
        this.bloodType = new BloodTypeDTO(patient.getBloodType());
        this.ssn = patient.getSsn();
        this.doctorId = patient.getDoctor().getId();
        this.id = patient.getId();
    }
}
