package com.easyhelp.application.utils.exceptions;

public class EasyHelpException extends Exception {
    public EasyHelpException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
