package com.easyhelp.application.model.locations;


import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"donationCenterPersonnelSet", "donations", "donationBookings", "donationCommitments", "storedBloodSet"})
@NoArgsConstructor
@Table(name = "donation_centers")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DonationCenter extends RealLocation {

    @OneToMany(mappedBy = "donationCenter", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<DonationCenterPersonnel> donationCenterPersonnelSet = new HashSet<>();

    @OneToMany(mappedBy = "donationCenter", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Donation> donations = new HashSet<>();

    @OneToMany(mappedBy = "donationCenter", orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<DonationBooking> donationBookings = new HashSet<>();

    @OneToMany(mappedBy = "donationCenter")
    private Set<DonationCommitment> donationCommitments = new HashSet<>();

    @OneToMany(mappedBy = "donationCenter", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<StoredBlood> storedBloodSet = new HashSet<>();

    private Integer numberOfConcurrentDonors = 3;

    public DonationCenter(String name, double longitude, double latitude, String address, County county) {
        super(name, longitude, latitude, address, county);
    }

    public boolean canBeRemoved() {
        return donationCenterPersonnelSet.isEmpty()
                && donations.isEmpty()
                && donationBookings.isEmpty()
                && donationCommitments.isEmpty()
                && storedBloodSet.isEmpty();
    }

    public void addBooking(DonationBooking donationBooking) {
        if (donationBookings == null) {
            donationBookings = new HashSet<>();
        }
        donationBookings.add(donationBooking);
    }
}
