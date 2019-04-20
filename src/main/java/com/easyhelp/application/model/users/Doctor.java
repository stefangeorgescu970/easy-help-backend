package com.easyhelp.application.model.users;

import com.easyhelp.application.model.dto.account.RegisterDTO;
import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.Patient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @ToString.Exclude
    @JsonIgnore
    private Hospital hospital;

    @OneToMany(mappedBy = "doctor", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<DonationRequest> donationRequests = new HashSet<>();

    @OneToMany(mappedBy = "doctor", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Patient> patients = new HashSet<>();

    public Doctor(RegisterDTO applicationUser) {
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