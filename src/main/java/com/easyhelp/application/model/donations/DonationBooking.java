package com.easyhelp.application.model.donations;

import com.easyhelp.application.model.BaseEntity;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.users.Donor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "donation_bookings")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DonationBooking extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "fk_donor")
    private Donor donor;

    @ManyToOne
    @JoinColumn(name = "fk_donation_center")
    private DonationCenter donationCenter;

    private ZonedDateTime dateAndTime;

    @ManyToOne
    @JoinColumn(name = "fk_patient")
    private Patient patient;

    private Boolean isForPatient;

}
