package com.easyhelp.application.model.dto.account;

import lombok.Data;

@Data
public class RegisterDTO extends AccountDTO {
    private Long locationId;
    private String password;

    private Boolean skipSsnValidation;
}
