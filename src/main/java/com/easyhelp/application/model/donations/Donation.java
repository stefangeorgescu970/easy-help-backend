package com.easyhelp.application.model.donations;

import com.easyhelp.application.model.BaseEntity;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.users.Donor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "donations")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Donation extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_donor")
    private Donor donor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_donation_center")
    private DonationCenter donationCenter;

    @ManyToOne
    @JoinColumn(name = "fk_patient")
    private Patient patient;

    @OneToOne(mappedBy = "donation")
    private DonationTestResult testResults;

    private DonationStatus status;
    private LocalDate date;
    private Boolean withPatient;
}
