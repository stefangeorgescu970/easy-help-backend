package com.easyhelp.application.repository;


import com.easyhelp.application.model.users.SystemAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface SystemAdminRepository extends JpaRepository<SystemAdmin, Long> {

    SystemAdmin findByEmail(String username);
}
