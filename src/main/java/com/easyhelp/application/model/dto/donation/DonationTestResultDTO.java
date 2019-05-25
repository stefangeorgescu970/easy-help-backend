package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.donations.DonationTestResult;
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
    }

    public DonationTestResultDTO(DonationTestResult donationTestResult) {
        hepatitisB = donationTestResult.getHepatitisB();
        hepatitisC = donationTestResult.getHepatitisC();
        hiv = donationTestResult.getHiv();
        htlv = donationTestResult.getHtlv();
        vdrl = donationTestResult.getVdrl();
        alt = donationTestResult.getAlt();
    }

    public Boolean isFailed() {
        if (hasFailed != null)
            return hasFailed;

        else return hepatitisC || hepatitisB || hiv || htlv || vdrl || alt;
    }
}
