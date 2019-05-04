package com.easyhelp.application.model.dto.account;

import com.easyhelp.application.model.dto.misc.IdentifierDTO;
import com.easyhelp.application.model.users.AppPlatform;
import lombok.Data;

@Data
public class PushNotificationDTO extends IdentifierDTO {
    private String token;
    private AppPlatform appPlatform;
}
