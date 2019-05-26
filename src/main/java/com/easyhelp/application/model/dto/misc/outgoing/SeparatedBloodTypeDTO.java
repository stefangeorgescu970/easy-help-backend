package com.easyhelp.application.model.dto.misc.outgoing;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.blood.SeparatedBloodType;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;

@Data
public class SeparatedBloodTypeDTO extends BaseOutgoingDTO {

    private BloodTypeDTO bloodType;
    private BloodComponent component;

    public SeparatedBloodTypeDTO(SeparatedBloodType separatedBloodType) {
        this.bloodType = new BloodTypeDTO(separatedBloodType.getBloodType());
        this.component = separatedBloodType.getComponent();
    }
}
