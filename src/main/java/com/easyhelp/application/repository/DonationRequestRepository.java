package com.easyhelp.application.repository;

import com.easyhelp.application.model.requests.DonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface DonationRequestRepository extends JpaRepository<DonationRequest, Long> {
}