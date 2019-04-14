package com.easyhelp.application.model.dto.requests;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.account.DoctorAccountDTO;
import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.requests.RequestStatus;
import com.easyhelp.application.model.requests.RequestUrgency;
import lombok.Data;

@Data
public class DonationRequestDetailsDTO extends BaseDTO {

    private Long id;
    private DoctorAccountDTO doctor;
    private LocationDTO acceptingDonationCenter;
    private Patient patient;
    private BloodType bloodType;
    private BloodComponent bloodComponent;
    private Integer quantity;
    private RequestUrgency urgency;
    private RequestStatus status;

    public DonationRequestDetailsDTO(DonationRequest donationRequest) {
        this.id = donationRequest.getId();
        this.doctor = new DoctorAccountDTO(donationRequest.getDoctor());
//        this.patient = donationRequest.getPatient();
        this.quantity = donationRequest.getQuantity();
        this.urgency = donationRequest.getUrgency();
        this.status = donationRequest.getStatus();
        if (donationRequest.getAcceptingDonationCenter() != null)
            this.acceptingDonationCenter = new LocationDTO(donationRequest.getAcceptingDonationCenter());

        if (donationRequest.getSeparatedBloodType() != null){
            this.bloodType = donationRequest.getSeparatedBloodType().getBloodType();
            this.bloodComponent = donationRequest.getSeparatedBloodType().getComponent();
        }

    }
}
