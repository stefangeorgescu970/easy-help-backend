package com.easyhelp.application.service.bloodtype;

import com.easyhelp.application.model.blood.BloodType;

public interface BloodTypeServiceInterface {
    public BloodType findBloodTypeInDB(String groupLetter, Boolean rh);

    public BloodType saveBloodType(BloodType bloodType);
}
