package com.easyhelp.application.model.dto.dcp.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DonationCreationDTO extends BaseIncomingDTO {

    private Long bookingId;
    private String groupLetter;
    private Boolean rh;
}
