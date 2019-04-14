package com.easyhelp.application.model.requests;

import com.easyhelp.application.model.BaseEntity;
import com.easyhelp.application.model.blood.SeparatedBloodType;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.users.Doctor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "donation_requests")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DonationRequest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "fk_doctor")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "fk_donation_center")
    private DonationCenter acceptingDonationCenter;

    @ManyToOne
    @JoinColumn(name = "fk_patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "fk_blood_type")
    private SeparatedBloodType separatedBloodType;

    private Integer quantity;
    private RequestUrgency urgency;
    private RequestStatus status;

}
