package com.easyhelp.application.model.users;

import com.easyhelp.application.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ApplicationUser {

    //    private String firstName;
//    private String lastName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;
    private Date dateOfBirth;
    private String city;
    private String country;
    private String ssn;
    private UserType userType;
}
