package com.easyhelp.application.model.dto.donor.incoming;

import com.easyhelp.application.model.donations.DonationForm;
import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;

@Data
public class DonationFormCreateDTO extends BaseIncomingDTO {

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
}
