package com.easyhelp.application.model.requests;

import com.easyhelp.application.model.BaseEntity;
import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.users.Doctor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"doctor", "donations", "donationRequests", "bloodType", "donationBookings"})
@NoArgsConstructor
@Table(name = "patients")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Patient extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "fk_doctor")
    private Doctor doctor;

    @OneToMany(mappedBy = "patient", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Donation> donations = new HashSet<>();

    @OneToMany(mappedBy = "patient", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DonationRequest> donationRequests = new HashSet<>();

    @OneToMany(mappedBy = "patient", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DonationBooking> donationBookings = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "fk_blood_type")
    private BloodType bloodType;

    private String ssn;
}
