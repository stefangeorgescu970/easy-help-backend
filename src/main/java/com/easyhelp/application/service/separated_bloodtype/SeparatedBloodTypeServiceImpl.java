package com.easyhelp.application.service.separated_bloodtype;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.blood.SeparatedBloodType;
import com.easyhelp.application.repository.SeparatedBloodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeparatedBloodTypeServiceImpl implements SeparatedBloodTypeServiceInterface {

    @Autowired
    private SeparatedBloodTypeRepository separatedBloodTypeRepository;

    @Override
    public SeparatedBloodType findSeparatedBloodTypeInDB(String groupLetter, Boolean rh, BloodComponent bloodComponent) {
        for (SeparatedBloodType separatedBloodType : separatedBloodTypeRepository.findAll()) {
            if (separatedBloodType.getBloodType().getRh() == rh &&
                    separatedBloodType.getBloodType().getGroupLetter().equals(groupLetter) &&
                    separatedBloodType.getComponent().equals(bloodComponent))
                return separatedBloodType;
        }
        return null;
    }

    @Override
    public void save(SeparatedBloodType separatedBloodType) {
        separatedBloodTypeRepository.save(separatedBloodType);
    }
}
