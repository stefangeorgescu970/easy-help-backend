package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

@Data
public class BloodGroupRhDTO extends BaseDTO {
    private String groupLetter;
    private Boolean rh;
    private Long donorId;
}
