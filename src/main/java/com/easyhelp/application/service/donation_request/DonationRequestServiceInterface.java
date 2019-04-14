package com.easyhelp.application.service.donation_request;

import com.easyhelp.application.model.dto.requests.DonationRequestDTO;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface  DonationRequestServiceInterface  {
    List<DonationRequest> getAll();

    void requestDonation(DonationRequestDTO donationRequest) throws EntityNotFoundException, EntityAlreadyExistsException;

    List<DonationRequest> getAllRequestsForDoctor(Long doctorId);
}
