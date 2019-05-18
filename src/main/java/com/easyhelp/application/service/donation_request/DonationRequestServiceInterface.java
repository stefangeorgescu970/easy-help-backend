package com.easyhelp.application.service.donation_request;

import com.easyhelp.application.model.dto.donation.DonationCommitmentCreateDTO;
import com.easyhelp.application.model.dto.requests.DonationRequestDTO;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface  DonationRequestServiceInterface  {
    List<DonationRequest> getAll();

    void requestDonation(DonationRequestDTO donationRequest) throws EntityNotFoundException, EntityAlreadyExistsException;

    void markRequestAsFinished(DonationRequest donationRequest, DonationCommitment finalCommitment);

    void saveRequest(DonationRequest donationRequest);

    List<DonationRequest> getAllRequestsForDoctor(Long doctorId);

    List<DonationRequest> getAllRequestsForDC(Long donationCenterId) throws EntityNotFoundException;

    List<DonationRequest> getAllRequestsForPatient(Long patientId);

    void commitToDonation(DonationCommitmentCreateDTO donationCommitmentCreateDTO) throws EasyHelpException;
}
