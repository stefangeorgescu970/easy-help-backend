package com.easyhelp.application.repository;

import com.easyhelp.application.model.blood.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface BloodTypeRepository extends JpaRepository<BloodType, Long> {
}
