package com.easyhelp.application.model.dto.doctor.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.BloodTypeDTO;
import lombok.Data;

@Data
public class DoctorPatientCreateDTO extends BaseIncomingDTO {
    private String ssn;
    private BloodTypeDTO bloodType;
    private Long doctorId;
}
