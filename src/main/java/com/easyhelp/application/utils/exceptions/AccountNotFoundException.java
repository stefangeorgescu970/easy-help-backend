package com.easyhelp.application.utils.exceptions;

public class AccountNotFoundException extends EasyHelpException {
    public AccountNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

