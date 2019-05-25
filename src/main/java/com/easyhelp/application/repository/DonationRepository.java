package com.easyhelp.application.repository;

import com.easyhelp.application.model.donations.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface DonationRepository extends JpaRepository<Donation, Long> {

}
