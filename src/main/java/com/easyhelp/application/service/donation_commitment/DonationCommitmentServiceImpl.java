package com.easyhelp.application.service.donation_commitment;

import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationCommitmentStatus;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.RequestStatus;
import com.easyhelp.application.repository.DonationCommitmentRepository;
import com.easyhelp.application.service.donation_request.DonationRequestServiceInterface;
import com.easyhelp.application.service.stored_blood.StoredBloodServiceInterface;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DonationCommitmentServiceImpl implements DonationCommitmentServiceInterface {

    @Autowired
    private DonationCommitmentRepository donationCommitmentRepository;

    @Autowired
    private StoredBloodServiceInterface storedBloodService;

    @Autowired
    private DonationRequestServiceInterface donationRequestService;

    @Override
    public void save(DonationCommitment donationCommitment) {
        donationCommitmentRepository.save(donationCommitment);
    }

    @Override
    public List<DonationCommitment> getDonationCommitments(Long donationRequestId) throws EntityNotFoundException {
        return donationCommitmentRepository.findAll()
                .stream()
                .filter(donationCommitment -> donationCommitment.getDonationRequest().getId().equals(donationRequestId))
                .collect(Collectors.toList());
    }

    @Override
    public void acceptCommitment(Long donationCommitmentId) throws EasyHelpException {
        Optional<DonationCommitment> donationCommitmentOptional = donationCommitmentRepository.findById(donationCommitmentId);

        if (!donationCommitmentOptional.isPresent()) {
            throw new EntityNotFoundException("Donation commitment with this id does not exist");
        }

        DonationCommitment donationCommitment = donationCommitmentOptional.get();

        if (!donationCommitment.getStatus().equals(DonationCommitmentStatus.COMMITTED_BY_DONATION_CENTER))
            throw new EasyHelpException("This donation commitment has been already accepted by a doctor");

        DonationRequest donationRequest = donationCommitment.getDonationRequest();

        Double currentCommittedTotal = donationRequest
                .getDonationCommitments()
                .stream()
                .filter(dc -> dc.getStatus().equals(DonationCommitmentStatus.ACCEPTED_BY_DOCTOR) || dc.getStatus().equals(DonationCommitmentStatus.SHIPPED_BY_DONATION_CENTER) || dc.getStatus().equals(DonationCommitmentStatus.ARRIVED_AT_DOCTOR))
                .mapToDouble(dc -> dc.getStoredBlood().getAmount())
                .sum();

        if (currentCommittedTotal >= donationRequest.getQuantity()) {
            throw new EasyHelpException("This donation request already has enough commitments to be completed.");
        }

        donationCommitment.setStatus(DonationCommitmentStatus.ACCEPTED_BY_DOCTOR);
        donationCommitmentRepository.save(donationCommitment);

        currentCommittedTotal += donationCommitment.getStoredBlood().getAmount();

        if (currentCommittedTotal >= donationRequest.getQuantity()) {
            donationRequest.setStatus(RequestStatus.FULLY_COMMITTED_TO);
        } else {
            donationRequest.setStatus(RequestStatus.PARTIALLY_COMMITTED_TO);
        }

        donationRequestService.saveRequest(donationRequest);
    }

    @Override
    public void shipCommitment(Long donationCommitmentId) throws EasyHelpException {
        Optional<DonationCommitment> donationCommitmentOptional = donationCommitmentRepository.findById(donationCommitmentId);

        if (!donationCommitmentOptional.isPresent()) {
            throw new EntityNotFoundException("Donation commitment with this id does not exist");
        }

        DonationCommitment donationCommitment = donationCommitmentOptional.get();

        if (!donationCommitment.getStatus().equals(DonationCommitmentStatus.ACCEPTED_BY_DOCTOR))
            throw new EasyHelpException("This donation commitment has been already shipped");

        donationCommitment.setStatus(DonationCommitmentStatus.SHIPPED_BY_DONATION_CENTER);
        donationCommitmentRepository.save(donationCommitment);
    }

    @Override
    public void markCommitmentAsArrived(Long donationCommitmentId) throws EasyHelpException {
        Optional<DonationCommitment> donationCommitmentOptional = donationCommitmentRepository.findById(donationCommitmentId);

        if (!donationCommitmentOptional.isPresent()) {
            throw new EntityNotFoundException("Donation commitment with this id does not exist");
        }

        DonationCommitment donationCommitment = donationCommitmentOptional.get();

        if (!donationCommitment.getStatus().equals(DonationCommitmentStatus.SHIPPED_BY_DONATION_CENTER))
            throw new EasyHelpException("This donation commitment has already arrived");

        // Set Quantity on Request or delete request
        DonationRequest donationRequest = donationCommitment.getDonationRequest();
        Double remainingNeededBlood = donationRequest.getQuantity() - donationCommitment.getStoredBlood().getAmount();
        if (remainingNeededBlood.equals(0.0)) {
            donationRequestService.markRequestAsFinished(donationRequest, donationCommitment);
        } else {
            donationRequest.setQuantity(remainingNeededBlood);
            donationRequestService.saveRequest(donationRequest);
        }


        donationCommitment.setStatus(DonationCommitmentStatus.ARRIVED_AT_DOCTOR);
        donationCommitmentRepository.save(donationCommitment);

        // Mark Stored Blood as Arrived
        StoredBlood storedBlood = donationCommitment.getStoredBlood();
        storedBlood.setUsedDate(new Date());
        storedBloodService.storeBlood(storedBlood);
    }

    @Override
    public List<DonationCommitment> getCommitmentsForDonationCenter(DonationCenter donationCenter, DonationCommitmentStatus withStatus) {
        return donationCommitmentRepository.findAll()
                .stream()
                .filter(donationCommitment -> donationCommitment.getStatus().equals(withStatus))
                .collect(Collectors.toList());
    }
}
