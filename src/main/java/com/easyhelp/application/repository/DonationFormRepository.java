package com.easyhelp.application.repository;

import com.easyhelp.application.model.donations.DonationForm;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface DonationFormRepository extends JpaRepository<DonationForm, Long> {
}
