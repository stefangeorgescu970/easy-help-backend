package com.easyhelp.application.model.dto.donor.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import lombok.Data;

@Data
public class LocalizationDTO extends BaseIncomingDTO {
    private Double latitude;
    private Double longitude;
}
