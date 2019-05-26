package com.easyhelp.application.model.dto.donor.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.locations.County;
import lombok.Data;

@Data
public class CountySsnDTO extends BaseIncomingDTO {
    private String ssn;
    private County county;
    private Boolean skipSsnCheck;
}
