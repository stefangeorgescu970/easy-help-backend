package com.easyhelp.application.repository;

import com.easyhelp.application.model.users.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Doctor findByEmail(String username);
}
