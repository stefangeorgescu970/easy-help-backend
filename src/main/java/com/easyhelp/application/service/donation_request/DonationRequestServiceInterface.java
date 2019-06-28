package com.easyhelp.application.service.donation_request;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.dto.dcp.incoming.DonationCommitmentCreateDTO;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.RequestUrgency;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface  DonationRequestServiceInterface  {
    List<DonationRequest> getAll();

    DonationRequest requestDonation(Long doctorId, Long patientId, Double quantity, RequestUrgency urgency, BloodComponent bloodComponent) throws EntityNotFoundException, EntityAlreadyExistsException;

    void markRequestAsFinished(DonationRequest donationRequest, DonationCommitment finalCommitment);

    DonationRequest saveRequest(DonationRequest donationRequest);

    List<DonationRequest> getAllRequestsForDoctor(Long doctorId);

    List<DonationRequest> getAllRequestsForDC(Long donationCenterId) throws EntityNotFoundException;

    List<DonationRequest> getAllRequestsForPatient(Long patientId);

    DonationCommitment commitToDonation(DonationCommitmentCreateDTO donationCommitmentCreateDTO) throws EasyHelpException;

    void cancelRequest(Long requestId) throws EasyHelpException;

    List<DonationRequest> getDonationRequestsDonorCouldDonateFor(Donor donor);

    DonationRequest findById(Long donationRequestId) throws EntityNotFoundException;
}
