package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.donations.DonationForm;
import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

@Data
public class DonationFormDTO extends BaseDTO {

    private Long donorId;

    private Boolean generalGoodHealth;
    private Boolean recentLossOfWeight;
    private Boolean recentInexplicableFever;
    private Boolean recentStomatoTreatmentOrVaccine;
    private Boolean currentDrugTreatment;
    private Boolean sexWithHIVOrHepatitisLast12Months;
    private Boolean sexWithPersonWhoInjectsDrugsLast12Months;
    private Boolean sexWithProstituteLast12Months;
    private Boolean sexWithMultiplePartnersLast12Months;
    private Boolean injectedDrugs;
    private Boolean acceptedMoneyOrDrugsForSex;
    private Boolean changedSexPartnerLast6Months;
    private Boolean surgeryOrInvestigationsLast12Months;
    private Boolean tattoosOrPiercingsLast12Months;
    private Boolean transfusionLast12Months;
    private Boolean beenPregnant;
    private Boolean bornLivedTraveledAbroad;
    private Boolean prisonLastYear;
    private Boolean exposedHepatitis;
    private Boolean sufferFromSet1;
    private Boolean sufferFromSet2;
    private Boolean sufferFromSet3;
    private Boolean sufferFromSet4;
    private Boolean sufferFromSet5;
    private Boolean sufferFromSet6;
    private Boolean sufferFromSet7;
    private Boolean smoker;
    private Boolean beenRefused;
    private Boolean requireAttentionPostDonation;

    private Integer numberOfPartnersLast6Months;

    private String birthDate;
    private String lastMenstruation;
    private String lastAlcoholUse;

    private String travelWhere;
    private String travelWhen;
    private String alcoholDrank;
    private String alcoholQuantity;

    public DonationFormDTO() {
    }

    public DonationFormDTO(DonationForm donationForm) {
        donorId = donationForm.getDonor().getId();
        generalGoodHealth = donationForm.getGeneralGoodHealth();
        recentLossOfWeight = donationForm.getRecentLossOfWeight();
        recentInexplicableFever = donationForm.getRecentInexplicableFever();
        currentDrugTreatment = donationForm.getCurrentDrugTreatment();
        sexWithHIVOrHepatitisLast12Months = donationForm.getSexWithHIVOrHepatitisLast12Months();
        sexWithPersonWhoInjectsDrugsLast12Months = donationForm.getSexWithPersonWhoInjectsDrugsLast12Months();
        sexWithProstituteLast12Months = donationForm.getSexWithProstituteLast12Months();
        sexWithMultiplePartnersLast12Months = donationForm.getSexWithMultiplePartnersLast12Months();
        injectedDrugs = donationForm.getInjectedDrugs();
        acceptedMoneyOrDrugsForSex = donationForm.getAcceptedMoneyOrDrugsForSex();
        changedSexPartnerLast6Months = donationForm.getChangedSexPartnerLast6Months();
        surgeryOrInvestigationsLast12Months = donationForm.getSurgeryOrInvestigationsLast12Months();
        tattoosOrPiercingsLast12Months = donationForm.getTattoosOrPiercingsLast12Months();
        transfusionLast12Months = donationForm.getTransfusionLast12Months();
        beenPregnant = donationForm.getBeenPregnant();
        bornLivedTraveledAbroad = donationForm.getBornLivedTraveledAbroad();
        prisonLastYear = donationForm.getPrisonLastYear();
        exposedHepatitis = donationForm.getExposedHepatitis();
        sufferFromSet1 = donationForm.getSufferFromSet1();
        sufferFromSet2 = donationForm.getSufferFromSet2();
        sufferFromSet3 = donationForm.getSufferFromSet3();
        sufferFromSet4 = donationForm.getSufferFromSet4();
        sufferFromSet5 = donationForm.getSufferFromSet5();
        sufferFromSet6 = donationForm.getSufferFromSet6();
        sufferFromSet7 = donationForm.getSufferFromSet7();
        smoker = donationForm.getSmoker();
        beenRefused = donationForm.getBeenRefused();
        requireAttentionPostDonation = donationForm.getRequireAttentionPostDonation();

        numberOfPartnersLast6Months = donationForm.getNumberOfPartnersLast6Months();

        birthDate = donationForm.getBirthDate();
        lastMenstruation = donationForm.getLastMenstruation();
        lastAlcoholUse = donationForm.getLastAlcoholUse();

        travelWhere = donationForm.getTravelWhere();
        travelWhen = donationForm.getTravelWhen();
        alcoholDrank = donationForm.getAlcoholDrank();
        alcoholQuantity = donationForm.getAlcoholQuantity();
    }
}
