package com.easyhelp.application.repository;

import com.easyhelp.application.model.blood.StoredBlood;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


@Transactional
public interface StoredBloodRepository extends JpaRepository<StoredBlood, Long> {
}
