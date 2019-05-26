package com.easyhelp.application.model.dto;

import com.easyhelp.application.model.users.UserType;
import lombok.Data;

@Data
public class BaseIncomingDTO extends BaseOutgoingDTO {
    private Long userId;
    private UserType userType;
}
