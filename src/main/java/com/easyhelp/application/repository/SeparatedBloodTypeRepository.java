package com.easyhelp.application.repository;

import com.easyhelp.application.model.blood.SeparatedBloodType;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface SeparatedBloodTypeRepository extends JpaRepository<SeparatedBloodType, Long> {
}
