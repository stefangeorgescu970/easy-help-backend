package com.easyhelp.application.model.dto.filter;

import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.locations.County;
import lombok.Data;

@Data
public class FilterDonorDTO extends BaseDTO {

    private County county;
    private Boolean canDonate;
    private String groupLetter;

}
