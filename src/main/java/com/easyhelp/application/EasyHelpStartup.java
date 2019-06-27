package com.easyhelp.application;

import com.easyhelp.application.model.dto.auth.RegisterDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.users.UserType;
import com.easyhelp.application.service.RegisterService;
import com.easyhelp.application.service.applicationuser.ApplicationUserService;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;
import com.easyhelp.application.utils.exceptions.UserAlreadyRegisteredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
public class EasyHelpStartup implements ApplicationListener<ApplicationReadyEvent> {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private ApplicationUserService applicationUserService;

    public EasyHelpStartup(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        try {
            try {
                applicationUserService.findByEmailInAllUsers("admin");
            } catch (UsernameNotFoundException e) {
                addSysAdmin();
            }
        } catch (UserAlreadyRegisteredException | SsnInvalidException | EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addSysAdmin() throws EntityNotFoundException, UserAlreadyRegisteredException, SsnInvalidException {
        registerService.registerUser(createSysAdmin("Admin", "Adminovici", County.CLUJ, "admin", "1940205248591"));
    }

    private RegisterDTO createSysAdmin(String fn, String ln, County county, String email, String ssn) {
        RegisterDTO sysAdmin = new RegisterDTO();
        sysAdmin.setSkipSsnValidation(true);
        sysAdmin.setFirstName(fn);
        sysAdmin.setLastName(ln);
        sysAdmin.setCounty(county);
        sysAdmin.setEmail(email);
        sysAdmin.setPassword(bCryptPasswordEncoder.encode("admin"));
        sysAdmin.setSsn(ssn);
        sysAdmin.setDateOfBirth(MiscUtils.getDataFromSsn(ssn).getDateOfBirth());
        sysAdmin.setUserType(UserType.SYSADMIN);

        return sysAdmin;
    }
}
