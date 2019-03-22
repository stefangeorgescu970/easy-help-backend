package com.easyhelp.application.utils.exceptions;

public class UserAlreadyRegisteredException extends EasyHelpException {
    public UserAlreadyRegisteredException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
