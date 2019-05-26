package com.easyhelp.application.model.dto.doctor.incoming;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.requests.RequestUrgency;
import lombok.Data;

@Data
public class DonationRequestCreateDTO extends BaseIncomingDTO {
    private Long doctorId;
    private Long patientId;
    private Double quantity;
    private RequestUrgency urgency;
    private BloodComponent bloodComponent;
}
