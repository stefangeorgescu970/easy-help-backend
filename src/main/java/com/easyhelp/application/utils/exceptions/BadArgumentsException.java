package com.easyhelp.application.utils.exceptions;

public class BadArgumentsException extends EasyHelpException {
    public BadArgumentsException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
