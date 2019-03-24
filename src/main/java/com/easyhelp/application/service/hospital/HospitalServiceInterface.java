package com.easyhelp.application.service.hospital;


import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.utils.exceptions.EntityCannotBeRemovedException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface HospitalServiceInterface {
    List<Hospital> getAll();

    void save(Hospital hospital);

    void removeHospital(Long hospitalId) throws EntityCannotBeRemovedException, EntityNotFoundException;

    Hospital findById(Long hospitalId) throws EntityNotFoundException;

    List<Hospital> getHospitalsInCounty(County county);
}
