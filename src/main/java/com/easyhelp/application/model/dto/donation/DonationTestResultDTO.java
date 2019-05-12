package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

@Data
public class DonationTestResultDTO extends BaseDTO {
    private Long donationId;

    private Boolean hepatitisB;
    private Boolean hepatitisC;
    private Boolean hiv;
    private Boolean htlv;
    private Boolean vdrl;
    private Boolean alt;

    private Boolean hasFailed;

    public DonationTestResultDTO() {
        hepatitisB = false;
        hepatitisC = false;
        hiv = false;
        htlv = false;
        vdrl = false;
        alt = false;

        hasFailed = false;
    }
}
