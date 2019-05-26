package com.easyhelp.application.model.dto.misc;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDTO extends BaseOutgoingDTO {
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
