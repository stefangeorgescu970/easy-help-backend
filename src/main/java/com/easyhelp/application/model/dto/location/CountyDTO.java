package com.easyhelp.application.model.dto.location;

import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.locations.County;
import lombok.Data;

@Data
public class CountyDTO extends BaseDTO {
    private County county;
}
