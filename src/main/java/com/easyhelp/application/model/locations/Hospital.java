package com.easyhelp.application.model.locations;

import com.easyhelp.application.model.users.Doctor;
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
@EqualsAndHashCode(callSuper = true, exclude = {"doctors"})
@NoArgsConstructor
@Table(name = "hospitals")
public class Hospital extends RealLocation {

    @OneToMany(mappedBy = "hospital", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private Set<Doctor> doctors = new HashSet<>();

    public Hospital(String name, double longitude, double latitude, String address, County county) {
        super(name, longitude, latitude, address, county);
    }

    public boolean canBeRemoved() {
        return this.doctors.isEmpty();
    }
}
