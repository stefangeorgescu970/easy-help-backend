package com.easyhelp.application.model.users;

import com.easyhelp.application.model.dto.account.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements Serializable {

    private AccountDTO user;
    private String token;
}
