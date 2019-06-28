package com.easyhelp.application.model.dto.donor.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;

@Data
public class BloodGroupRhDTO extends BaseIncomingDTO {
    private String groupLetter;
    private Boolean rh;
}
