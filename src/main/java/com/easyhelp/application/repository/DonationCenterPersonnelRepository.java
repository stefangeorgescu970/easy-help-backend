package com.easyhelp.application.repository;


import com.easyhelp.application.model.users.DonationCenterPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface DonationCenterPersonnelRepository extends JpaRepository<DonationCenterPersonnel, Long> {

    DonationCenterPersonnel findByEmail(String username);
}
