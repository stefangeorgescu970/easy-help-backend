package com.easyhelp.application.model.users;

import com.easyhelp.application.model.dto.account.RegisterDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"donationCenter"})
@Table(name = "donation_center_personnels")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DonationCenterPersonnel extends PartnerUser {

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_donation_center")
    @ToString.Exclude
    @JsonIgnore
    private DonationCenter donationCenter;

    public DonationCenterPersonnel(RegisterDTO applicationUser) {
        setCounty(applicationUser.getCounty());
        setDateOfBirth(applicationUser.getDateOfBirth());
        setEmail(applicationUser.getEmail());
        setFirstName(applicationUser.getFirstName());
        setLastName(applicationUser.getLastName());
        setPassword(applicationUser.getPassword());
        setSsn(applicationUser.getSsn());
        setUserType(applicationUser.getUserType());
        setIsReviewed(false);
        setIsValid(false);
    }
}
