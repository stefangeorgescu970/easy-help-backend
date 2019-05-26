package com.easyhelp.application.model.dto.doctor.outgoing;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.requests.Patient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoctorPatientLevel1DTO extends BaseOutgoingDTO {
    private String ssn;
    private Long id;

    public DoctorPatientLevel1DTO(Patient patient) {
        this.ssn = patient.getSsn();
        this.id = patient.getId();
    }
}
