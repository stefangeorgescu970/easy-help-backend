package com.easyhelp.application.model.dto.requests;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.RequestUrgency;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DonationRequestDTO extends BaseDTO {

    private Long doctorId;
    private Long patientId;
    private Double quantity;
    private RequestUrgency urgency;
    private BloodComponent bloodComponent;
}
