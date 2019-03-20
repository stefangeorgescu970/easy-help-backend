package com.easyhelp.application.service.hospital;

import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospitalServiceImpl implements HospitalServiceInterface {

    @Autowired
    HospitalRepository hospitalRepository;

    @Override
    public List<LocationDTO> getAll() {
        return hospitalRepository.findAll().stream().map(LocationDTO::new).collect(Collectors.toList());
    }

    @Override
    public void save(Hospital hospital) {
        hospitalRepository.save(hospital);
    }
}
