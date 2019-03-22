package com.easyhelp.application.service.hospital;


import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.locations.Hospital;

import java.util.List;

public interface HospitalServiceInterface {
    List<LocationDTO> getAll();

    void save(Hospital hospital);
}
