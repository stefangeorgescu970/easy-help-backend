package com.easyhelp.application.model.dto.doctor.outgoing;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.SeparatedBloodTypeDTO;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.RequestStatus;
import com.easyhelp.application.model.requests.RequestUrgency;
import lombok.Data;

@Data
public class DoctorDonationRequestDetailsDTO extends BaseOutgoingDTO {
    private Long id;
    private DoctorPatientLevel1DTO patient;
    private SeparatedBloodTypeDTO separatedBloodType;
    private Double quantity;
    private RequestUrgency urgency;
    private RequestStatus status;

    public DoctorDonationRequestDetailsDTO(DonationRequest donationRequest) {
        this.id = donationRequest.getId();

        this.quantity = donationRequest.getQuantity();
        this.urgency = donationRequest.getUrgency();
        this.status = donationRequest.getStatus();
        this.patient = new DoctorPatientLevel1DTO(donationRequest.getPatient());

        if (donationRequest.getSeparatedBloodType() != null) {
            this.separatedBloodType = new SeparatedBloodTypeDTO(donationRequest.getSeparatedBloodType());
        }
    }
}
