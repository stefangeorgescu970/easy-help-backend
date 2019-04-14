package com.easyhelp.application.model.dto.blood;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BloodTypeDTO extends BaseDTO {

    private String groupLetter;
    private Boolean rh;

    public BloodTypeDTO(BloodType bloodType) {
        this.groupLetter = bloodType.getGroupLetter();
        this.rh = bloodType.getRh();
    }
}
