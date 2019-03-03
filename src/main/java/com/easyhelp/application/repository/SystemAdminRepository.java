package com.easyhelp.application.repository;


import com.easyhelp.application.model.users.SystemAdmin;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface SystemAdminRepository extends CrudRepository<SystemAdmin, Long> {

}
