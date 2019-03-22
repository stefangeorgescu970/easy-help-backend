package com.easyhelp.application.model.users;

import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.Patient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"hospital", "donationRequests", "patients"})
@Entity
@Data
@NoArgsConstructor
@Table(name = "doctors")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Doctor extends PartnerUser {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_hospital")
    private Hospital hospital;

    @OneToMany(mappedBy = "doctor", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<DonationRequest> donationRequests = new HashSet<>();

    @OneToMany(mappedBy = "doctor", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Patient> patients = new HashSet<>();
}