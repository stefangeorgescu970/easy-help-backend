package com.easyhelp.application.utils.exceptions;

public class EntityAlreadyExistsException extends EasyHelpException {
    public EntityAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}