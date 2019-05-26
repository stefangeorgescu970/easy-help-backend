package com.easyhelp.application.model.dto.misc.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.locations.County;
import lombok.Data;

@Data
public class CountyDTO extends BaseIncomingDTO {
    private County county;
}
