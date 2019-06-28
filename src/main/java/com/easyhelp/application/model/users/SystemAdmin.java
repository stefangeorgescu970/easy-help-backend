package com.easyhelp.application.model.users;


import com.easyhelp.application.model.dto.auth.RegisterDTO;
import com.easyhelp.application.model.misc.SsnData;
import com.easyhelp.application.utils.MiscUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "system_admins")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SystemAdmin extends ApplicationUser {

    public SystemAdmin(RegisterDTO applicationUser) {
        setCounty(applicationUser.getCounty());
        setEmail(applicationUser.getEmail());
        setFirstName(applicationUser.getFirstName());
        setLastName(applicationUser.getLastName());
        setPassword(applicationUser.getPassword());
        setSsn(applicationUser.getSsn());
        String ssn = applicationUser.getSsn();
        SsnData ssnData = MiscUtils.getDataFromSsn(ssn);
        setDateOfBirth(ssnData.getDateOfBirth());
        setUserType(applicationUser.getUserType());
    }
}
