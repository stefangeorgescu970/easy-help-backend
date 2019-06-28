package com.easyhelp.application.model.dto.dcp.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import lombok.Data;

@Data
public class DonationTestResultCreateDTO extends BaseIncomingDTO {
    private Long donationId;

    private Boolean hepatitisB;
    private Boolean hepatitisC;
    private Boolean hiv;
    private Boolean htlv;
    private Boolean vdrl;
    private Boolean alt;

    private Boolean hasFailed;

    public Boolean isFailed() {
        if (hasFailed != null)
            return hasFailed;

        else return hepatitisC || hepatitisB || hiv || htlv || vdrl || alt;
    }
}
