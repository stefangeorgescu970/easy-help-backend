package com.easyhelp.application.model.dto.misc.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.locations.County;
import lombok.Data;

@Data
public class BaseIncomingLocationDTO extends BaseIncomingDTO {
    private String name;
    private String address;
    private County county;
    private Double longitude;
    private Double latitude;
    private String phone;
}
