package com.easyhelp.application.model.locations;

import com.easyhelp.application.model.dto.admin.incoming.AdminCreateHospitalDTO;
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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Hospital extends RealLocation {

    @OneToMany(mappedBy = "hospital", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Doctor> doctors = new HashSet<>();

    public Hospital(String name, double longitude, double latitude, String address, County county, String phone) {
        super(name, longitude, latitude, address, county, phone);
    }

    public Hospital(AdminCreateHospitalDTO location) {
        super(location.getName(), location.getLongitude(), location.getLatitude(), location.getAddress(), location.getCounty(), location.getPhone());
    }

    public boolean canBeRemoved() {
        return this.doctors.isEmpty();
    }
}
