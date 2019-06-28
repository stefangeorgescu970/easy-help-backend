package com.easyhelp.application.model.dto.dcp.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.locations.County;
import lombok.Data;

@Data
public class FilterDonorDTO extends BaseIncomingDTO {

    private County county;
    private Boolean canDonate;
    private String groupLetter;

}
