package com.easyhelp.application.model.dto.admin;

import com.easyhelp.application.model.dto.misc.incoming.BaseIncomingLocationDTO;
import lombok.Data;

@Data
public class AdminCreateDonationCenterDTO extends BaseIncomingLocationDTO {
    private Integer numberOfConcurrentDonors;
}
