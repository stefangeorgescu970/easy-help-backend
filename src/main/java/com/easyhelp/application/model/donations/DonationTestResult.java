package com.easyhelp.application.model.donations;

import com.easyhelp.application.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "donation_test_results")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DonationTestResult extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "fk_donation")
    private Donation donation;

    private Boolean hepatitisB;
    private Boolean hepatitisC;
    private Boolean hiv;
    private Boolean htlv;
    private Boolean vdrl;
    private Boolean alt;
}
