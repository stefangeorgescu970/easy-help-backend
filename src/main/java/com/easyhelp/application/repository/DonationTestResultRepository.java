package com.easyhelp.application.repository;

import com.easyhelp.application.model.donations.DonationTestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface DonationTestResultRepository extends JpaRepository<DonationTestResult, Long> {
}
