package com.easyhelp.application.model.dto.dcp.outgoing;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import com.easyhelp.application.model.dto.misc.outgoing.SeparatedBloodTypeDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.RequestStatus;
import com.easyhelp.application.model.requests.RequestUrgency;
import com.easyhelp.application.utils.MiscUtils;
import lombok.Data;

@Data
public class DCPDonationRequestDetailsDTO extends BaseOutgoingDTO {

    private Long id;
    private ExtendedOutgoingLocationDTO hospital;
    private SeparatedBloodTypeDTO separatedBloodType;
    private Double quantity;
    private RequestUrgency urgency;
    private RequestStatus status;
    private Integer distance;

    public DCPDonationRequestDetailsDTO(DonationRequest donationRequest, DonationCenter donationCenter) {
        this.id = donationRequest.getId();
        this.hospital = new ExtendedOutgoingLocationDTO(donationRequest.getDoctor().getHospital());
        this.quantity = donationRequest.getQuantity();
        this.urgency = donationRequest.getUrgency();
        this.status = donationRequest.getStatus();

        if (donationCenter != null) {
            this.distance = MiscUtils.computeDistance(donationCenter.getLatitude(), donationCenter.getLongitude(),
                    hospital.getLatitude(), hospital.getLongitude());
        }

        this.separatedBloodType = new SeparatedBloodTypeDTO(donationRequest.getSeparatedBloodType());
    }
}
