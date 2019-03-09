package com.easyhelp.application.repository;

import com.easyhelp.application.model.users.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    ApplicationUser findByEmail(String username);
}
