package com.easyhelp.application.model.users;

public enum UserType {
    DONOR, DONATION_CENTER_PERSONNEL, DOCTOR, SYSADMIN;

    public String getRole() {
        switch (this) {
            case DONOR:
                return "ROLE_DONOR";
            case DONATION_CENTER_PERSONNEL:
                return "ROLE_DCP";
            case SYSADMIN:
                return "ROLE_SYS_ADMIN";
            default:
                return "ROLE_DOCTOR";
        }
    }
}
