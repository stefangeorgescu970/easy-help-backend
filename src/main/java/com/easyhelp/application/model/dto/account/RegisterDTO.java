package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

@Data
public class RegisterDTO extends AccountDTO {
    private Long locationId;
    private String password;
}
