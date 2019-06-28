package com.easyhelp.application.model.dto.dcp.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;

@Data
public class DonationSplitResultCreateDTO extends BaseIncomingDTO {
    private Long donationId;

    private double plateletsUnits;
    private double redBloodCellsUnits;
    private double plasmaUnits;
}
