package com.easyhelp.application.model.dto.auth;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO extends BaseOutgoingDTO {

    private AccountDTO user;
    private String token;
}
