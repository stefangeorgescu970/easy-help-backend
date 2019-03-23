package com.easyhelp.application.model.users;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.DonationForm;
import com.easyhelp.application.model.dto.account.RegisterDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"donations", "donationBooking", "bloodType", "storedBloodSet"})
@Table(name = "donors")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Donor extends ApplicationUser {

    @OneToOne(mappedBy = "donor")
    @ToString.Exclude
    @JsonIgnore
    private DonationForm donationForm;

    @OneToMany(mappedBy = "donor", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private Set<Donation> donations;

    @OneToOne(mappedBy = "donor")
    @ToString.Exclude
    @JsonIgnore
    private DonationBooking donationBooking;

    @ManyToOne
    @JoinColumn(name = "fk_blood_type")
    @ToString.Exclude
    @JsonIgnore
    private BloodType bloodType;


    @OneToMany(mappedBy = "donor", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private Set<StoredBlood> storedBloodSet;

    public Donor(RegisterDTO applicationUser) {
        setCity(applicationUser.getCity());
        setDateOfBirth(applicationUser.getDob());
        setEmail(applicationUser.getEmail());
        setFirstName(applicationUser.getFirstName());
        setLastName(applicationUser.getLastName());
        setPassword(applicationUser.getPassword());
        setSsn(applicationUser.getSsn());
        setUserType(applicationUser.getUserType());
    }
}
