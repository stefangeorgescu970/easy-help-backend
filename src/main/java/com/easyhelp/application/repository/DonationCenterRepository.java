package com.easyhelp.application.repository;

import com.easyhelp.application.model.locations.DonationCenter;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface DonationCenterRepository extends JpaRepository<DonationCenter, Long> {
}
