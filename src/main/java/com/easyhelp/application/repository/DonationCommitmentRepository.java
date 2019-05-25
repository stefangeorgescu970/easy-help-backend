package com.easyhelp.application.repository;

import com.easyhelp.application.model.requests.DonationCommitment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface DonationCommitmentRepository extends JpaRepository<DonationCommitment, Long> {
}
