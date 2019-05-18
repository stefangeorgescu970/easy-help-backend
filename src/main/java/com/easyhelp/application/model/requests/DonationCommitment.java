package com.easyhelp.application.model.requests;


import com.easyhelp.application.model.BaseEntity;
import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.locations.DonationCenter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "donation_commitments")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DonationCommitment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_donation_center")
    private DonationCenter donationCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_donation_request")
    private DonationRequest donationRequest;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_stored_blood")
    private StoredBlood storedBlood;

    private DonationCommitmentStatus status;
}
