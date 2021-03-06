package com.easyhelp.application.model.blood;

import com.easyhelp.application.model.BaseEntity;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.users.Donor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"separatedBloodTypes", "donors", "patients"})
@NoArgsConstructor
@Table(name = "blood_type")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BloodType extends BaseEntity {

    @OneToMany(mappedBy = "bloodType", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Patient> patients;

    @OneToMany(mappedBy = "bloodType", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Donor> donors;

    @OneToMany(mappedBy = "bloodType", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<SeparatedBloodType> separatedBloodTypes;

    private String groupLetter;
    private Boolean rh;

    public BloodType(String groupLetter, Boolean rh) {
        this.groupLetter = groupLetter;
        this.rh = rh;
    }
}
