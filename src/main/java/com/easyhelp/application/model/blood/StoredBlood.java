package com.easyhelp.application.model.blood;

import com.easyhelp.application.model.BaseEntity;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.users.Donor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "stored_bloods")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class StoredBlood extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_donor")
    private Donor donor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_donation_center")
    private DonationCenter donationCenter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_blood_type")
    private SeparatedBloodType separatedBloodType;

    private Double amount;
    private Date storedDate;
}
