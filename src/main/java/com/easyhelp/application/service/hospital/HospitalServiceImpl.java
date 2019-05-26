package com.easyhelp.application.service.hospital;

import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.repository.HospitalRepository;
import com.easyhelp.application.utils.exceptions.EntityCannotBeRemovedException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HospitalServiceImpl implements HospitalServiceInterface {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public List<Hospital> getAll() {
        return new ArrayList<>(hospitalRepository.findAll());
    }

    @Override
    public void save(Hospital hospital) {
        hospitalRepository.save(hospital);
    }

    @Override
    public void removeHospital(Long hospitalId) throws EntityCannotBeRemovedException, EntityNotFoundException {
        Optional<Hospital> hospital = hospitalRepository.findById(hospitalId);

        if (hospital.isPresent()) {
            Hospital hospitalUnwrapped = hospital.get();

            if (!hospitalUnwrapped.canBeRemoved()) {
                throw new EntityCannotBeRemovedException("There are doctors linked to this hospital, so it cannot be deleted!");
            }

            hospitalRepository.deleteById(hospitalId);
        } else {
            throw new EntityNotFoundException("Hospital with that id does not exist");
        }
    }

    @Override
    public Hospital findById(Long hospitalId) throws EntityNotFoundException {
        Optional<Hospital> hospital = hospitalRepository.findById(hospitalId);

        if (hospital.isPresent()) {
            return hospital.get();
        } else {
            throw new EntityNotFoundException("Hospital with that id does not exist");
        }
    }

    @Override
    public List<Hospital> getHospitalsInCounty(County county) {
        return hospitalRepository
                .findAll()
                .stream()
                .filter(hospital -> hospital.getCounty() == county)
                .collect(Collectors.toList());
    }
}
