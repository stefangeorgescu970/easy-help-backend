package com.easyhelp.application.model.dto.misc.outgoing;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BloodTypeDTO extends BaseOutgoingDTO {

    private String groupLetter;
    private Boolean rh;

    public BloodTypeDTO(BloodType bloodType) {
        this.groupLetter = bloodType.getGroupLetter();
        this.rh = bloodType.getRh();
    }
}
