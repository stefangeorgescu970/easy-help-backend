package com.easyhelp.application.service.bloodtype;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.repository.BloodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloodTypeServiceImpl implements BloodTypeServiceInterface {

    @Autowired
    private BloodTypeRepository bloodTypeRepository;

    @Override
    public BloodType findBloodTypeInDB(String groupLetter, Boolean rh) {
        for (BloodType bloodType : bloodTypeRepository.findAll()) {
            if (bloodType.getRh() == rh && bloodType.getGroupLetter().equals(groupLetter)) {
                return bloodType;
            }
        }

        return null;
    }

    @Override
    public BloodType saveBloodType(BloodType bloodType) {
        return bloodTypeRepository.save(bloodType);
    }
}
