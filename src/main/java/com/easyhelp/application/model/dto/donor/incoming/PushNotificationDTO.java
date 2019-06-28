package com.easyhelp.application.model.dto.donor.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.dto.misc.incoming.IdentifierDTO;
import com.easyhelp.application.model.users.AppPlatform;
import lombok.Data;

@Data
public class PushNotificationDTO extends BaseIncomingDTO {
    private String token;
    private AppPlatform appPlatform;
}
