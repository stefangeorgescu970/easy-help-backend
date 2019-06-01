package com.easyhelp.application.model.users;

import com.easyhelp.application.model.locations.County;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private County county;
    private String ssn;
    private UserType userType;
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.ObtainVia
    private Set<String> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
