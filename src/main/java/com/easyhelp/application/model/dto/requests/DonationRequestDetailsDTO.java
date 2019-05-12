package com.easyhelp.application.model.dto.requests;

import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.dto.account.DoctorAccountDTO;
import com.easyhelp.application.model.dto.blood.SeparatedBloodTypeDTO;
import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.RequestStatus;
import com.easyhelp.application.model.requests.RequestUrgency;
import com.easyhelp.application.utils.MiscUtils;
import lombok.Data;

@Data
public class DonationRequestDetailsDTO extends BaseDTO {

    private Long id;
    private DoctorAccountDTO doctor;
    private PatientDTO patient;
    private SeparatedBloodTypeDTO separatedBloodTypeDTO;
    private Integer quantity;
    private RequestUrgency urgency;
    private RequestStatus status;

    private Integer distance;

    public DonationRequestDetailsDTO(DonationRequest donationRequest) {
        this.id = donationRequest.getId();
        this.doctor = new DoctorAccountDTO(donationRequest.getDoctor());
        this.quantity = donationRequest.getQuantity();
        this.urgency = donationRequest.getUrgency();
        this.status = donationRequest.getStatus();
        this.patient = new PatientDTO(donationRequest.getPatient());

        if (donationRequest.getSeparatedBloodType() != null) {
            this.separatedBloodTypeDTO = new SeparatedBloodTypeDTO(donationRequest.getSeparatedBloodType());
        }
    }

    public DonationRequestDetailsDTO(DonationRequest donationRequest, DonationCenter donationCenter) {
        this.id = donationRequest.getId();
        this.doctor = new DoctorAccountDTO(donationRequest.getDoctor());
        this.quantity = donationRequest.getQuantity();
        this.urgency = donationRequest.getUrgency();
        this.status = donationRequest.getStatus();
        this.patient = new PatientDTO(donationRequest.getPatient());

        if (donationCenter != null) {
            this.distance = MiscUtils.computeDistance(donationCenter.getLatitude(), donationCenter.getLongitude(),
                    doctor.getHospital().getLatitude(), doctor.getHospital().getLongitude());
        }

        if (donationRequest.getSeparatedBloodType() != null) {
            this.separatedBloodTypeDTO = new SeparatedBloodTypeDTO(donationRequest.getSeparatedBloodType());
        }
    }
}
