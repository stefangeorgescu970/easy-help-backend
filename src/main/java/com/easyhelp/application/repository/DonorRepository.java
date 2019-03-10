package com.easyhelp.application.repository;


import com.easyhelp.application.model.users.ApplicationUser;
import com.easyhelp.application.model.users.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface DonorRepository extends JpaRepository<Donor, Long> {

    Donor findByEmail(String username);
}
