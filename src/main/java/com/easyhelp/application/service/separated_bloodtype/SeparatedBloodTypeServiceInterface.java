package com.easyhelp.application.service.separated_bloodtype;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.blood.SeparatedBloodType;


public interface SeparatedBloodTypeServiceInterface {
    SeparatedBloodType findSeparatedBloodTypeInDB(String groupLetter, Boolean rh, BloodComponent bloodComponent);

    SeparatedBloodType save(SeparatedBloodType separatedBloodType);
}
