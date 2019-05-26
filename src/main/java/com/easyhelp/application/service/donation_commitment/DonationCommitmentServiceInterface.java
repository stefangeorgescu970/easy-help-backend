package com.easyhelp.application.service.donation_commitment;

import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationCommitmentStatus;
import com.easyhelp.application.model.requests.RequestStatus;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;


public interface DonationCommitmentServiceInterface {

    DonationCommitment save(DonationCommitment donationCommitment);

    List<DonationCommitment> getDonationCommitments(Long donationRequestId) throws EntityNotFoundException;

    RequestStatus acceptCommitment(Long donationCommitmentId) throws EasyHelpException;

    void declineCommitment(Long donationCommitmentId) throws EasyHelpException;

    void shipCommitment(Long donationCommitmentId) throws EasyHelpException;

    void markCommitmentAsArrived(Long donationCommitmentId) throws EasyHelpException;

    List<DonationCommitment> getCommitmentsForDonationCenter(DonationCenter donationCenter);
}
