package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

@Data
public class ActiveAccountDTO extends BaseDTO {
    private boolean active;
}

