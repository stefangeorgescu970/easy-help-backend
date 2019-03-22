package com.easyhelp.application.model.users;


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

}
