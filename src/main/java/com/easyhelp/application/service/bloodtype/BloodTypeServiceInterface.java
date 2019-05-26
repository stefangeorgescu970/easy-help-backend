package com.easyhelp.application.service.bloodtype;

import com.easyhelp.application.model.blood.BloodType;

public interface BloodTypeServiceInterface {
    BloodType findBloodTypeInDB(String groupLetter, Boolean rh);

    BloodType saveBloodType(BloodType bloodType);
}
