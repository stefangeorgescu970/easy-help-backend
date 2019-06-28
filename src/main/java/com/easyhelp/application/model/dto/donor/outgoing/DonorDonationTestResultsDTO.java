package com.easyhelp.application.model.dto.donor.outgoing;

import com.easyhelp.application.model.donations.DonationTestResult;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;

@Data
public class DonorDonationTestResultsDTO extends BaseOutgoingDTO {
    private Boolean hepatitisB;
    private Boolean hepatitisC;
    private Boolean hiv;
    private Boolean htlv;
    private Boolean vdrl;
    private Boolean alt;

    public DonorDonationTestResultsDTO(DonationTestResult donationTestResult) {
        hepatitisB = donationTestResult.getHepatitisB();
        hepatitisC = donationTestResult.getHepatitisC();
        hiv = donationTestResult.getHiv();
        htlv = donationTestResult.getHtlv();
        vdrl = donationTestResult.getVdrl();
        alt = donationTestResult.getAlt();
    }
}
