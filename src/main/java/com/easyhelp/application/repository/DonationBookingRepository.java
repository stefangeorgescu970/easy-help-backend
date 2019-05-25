package com.easyhelp.application.repository;

import com.easyhelp.application.model.donations.DonationBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface DonationBookingRepository extends JpaRepository<DonationBooking, Long> {
}
