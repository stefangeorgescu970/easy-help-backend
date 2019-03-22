package com.easyhelp.application.service.doctor;

import com.easyhelp.application.model.dto.account.DoctorAccountDTO;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.repository.DoctorRepository;
import com.easyhelp.application.utils.exceptions.AccountNotReviewedException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorServiceInterface {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public List<DoctorAccountDTO> getAllPendingAccounts() {
        return doctorRepository
                .findAll()
                .stream()
                .filter(doctor -> !doctor.getIsReviewed())
                .map(DoctorAccountDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorAccountDTO> getAllActiveAccounts() {
        return doctorRepository
                .findAll()
                .stream()
                .filter(doctor -> doctor.getIsReviewed() && doctor.getIsValid())
                .map(DoctorAccountDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorAccountDTO> getAllBannedAccounts() {
        return doctorRepository
                .findAll()
                .stream()
                .filter(doctor -> doctor.getIsReviewed() && !doctor.getIsValid())
                .map(DoctorAccountDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void reviewAccount(Long doctorId, boolean shouldValidate) throws EntityNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        if (doctor.isPresent()) {
            Doctor doctorUnwrapped = doctor.get();
            doctorUnwrapped.reviewAccount(shouldValidate);
            doctorRepository.save(doctorUnwrapped);
        } else {
            throw new EntityNotFoundException("user not found");
        }
    }

    @Override
    public void deactivateAccount(Long doctorId) throws AccountNotReviewedException, EntityNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        if (doctor.isPresent()) {
            Doctor doctorUnwrapped = doctor.get();
            doctorUnwrapped.invalidateAccount();
            doctorRepository.save(doctorUnwrapped);
        } else {
            throw new EntityNotFoundException("user not found");
        }
    }
}
