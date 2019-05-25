package com.easyhelp.application.model.dto.blood;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.blood.SeparatedBloodType;
import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

@Data
public class SeparatedBloodTypeDTO extends BaseDTO {

    private BloodTypeDTO bloodType;
    private BloodComponent component;

    public SeparatedBloodTypeDTO(SeparatedBloodType separatedBloodType) {
        this.bloodType = new BloodTypeDTO(separatedBloodType.getBloodType());
        this.component = separatedBloodType.getComponent();
    }
}
