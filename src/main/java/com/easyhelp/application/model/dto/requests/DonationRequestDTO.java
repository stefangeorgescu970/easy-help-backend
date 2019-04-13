package com.easyhelp.application.model.dto.requests;

import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.RequestStatus;
import com.easyhelp.application.model.requests.RequestUrgency;
import lombok.Data;

@Data

public class DonationRequestDTO extends BaseDTO {

    private Long doctorId;
    private Long patientId;
    private Integer quantity;
    private RequestUrgency urgency;

    public DonationRequestDTO(DonationRequest donationRequest) {
        this.doctorId = donationRequest.getDoctor().getId();
        this.patientId = donationRequest.getPatient().getId();
        this.quantity = donationRequest.getQuantity();
        this.urgency = donationRequest.getUrgency();
    }
}
