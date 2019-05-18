package com.easyhelp.application.model.blood;

import com.easyhelp.application.model.BaseEntity;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.users.Donor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"donor", "donationCenter", "separatedBloodType", "donationCommitment"})
@NoArgsConstructor
@Table(name = "stored_bloods")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class StoredBlood extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_donor")
    private Donor donor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_donation_center")
    private DonationCenter donationCenter;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_separated_blood_type")
    private SeparatedBloodType separatedBloodType;

    @OneToOne(mappedBy = "storedBlood", fetch = FetchType.LAZY)
    private DonationCommitment donationCommitment;

    private Double amount;
    private Date storedDate;

    private Date usedDate;
    private Boolean isUsable = true;
}
