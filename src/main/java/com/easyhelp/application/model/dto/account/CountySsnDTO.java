package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.locations.County;
import lombok.Data;

@Data
public class CountySsnDTO extends BaseDTO {
    private String ssn;
    private County county;
    private Long donorId;
    private Boolean skipSsnCheck;
}
