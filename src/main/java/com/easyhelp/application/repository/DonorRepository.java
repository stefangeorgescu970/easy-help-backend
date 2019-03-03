package com.easyhelp.application.repository;


import com.easyhelp.application.model.users.Donor;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface DonorRepository extends CrudRepository<Donor, Long> {

}
