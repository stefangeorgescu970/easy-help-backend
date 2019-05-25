package com.easyhelp.application.model.requests;

import com.easyhelp.application.model.BaseEntity;
import com.easyhelp.application.model.blood.SeparatedBloodType;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.users.Doctor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"doctor", "patient", "donationCommitments", "separatedBloodType"})
@NoArgsConstructor
@Table(name = "donation_requests")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DonationRequest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "fk_doctor")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "fk_patient")
    private Patient patient;

    @OneToMany(mappedBy = "donationRequest")
    private Set<DonationCommitment> donationCommitments = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "fk_separated_blood_type")
    private SeparatedBloodType separatedBloodType;

    private Double quantity;
    private RequestUrgency urgency;
    private RequestStatus status;
}
