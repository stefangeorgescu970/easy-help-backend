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
    private String bagIdentifier;

    private Date usedDate;
    private Boolean isUsable = true;

    public Boolean isExpired() {
        Date today = new Date();
        int days = (int) ((today.getTime() - storedDate.getTime()) / (1000 * 60 * 60 * 24));
        switch (separatedBloodType.getComponent()) {
            case RED_BLOOD_CELLS:
                return days > 42;
            case PLATELETS:
                return days > 5;
            case PLASMA:
                return days > 365;
            default:
                return true;
        }
    }
}
