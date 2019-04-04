package com.easyhelp.application.utils;

import com.easyhelp.application.utils.exceptions.SsnInvalidException;

import java.util.Date;

public class MiscUtils {
    private static Boolean validateSsn(String ssn) {
        return true;
    }

    public static Date getDateFromSsn(String ssn) throws SsnInvalidException {
        if (validateSsn(ssn)) {
            return new Date();
        } else {
            throw new SsnInvalidException("Provided SSN is not valid.");
        }
    }
}
