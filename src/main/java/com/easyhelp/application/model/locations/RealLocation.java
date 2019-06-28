package com.easyhelp.application.model.locations;


import com.easyhelp.application.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealLocation extends BaseEntity {
    private String name;
    private Double longitude;
    private Double latitude;
    private String address;
    private County county;
    private String phone;
}
