package com.easyhelp.application.model.dto.misc.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import lombok.Data;

@Data
public class StringDTO extends BaseIncomingDTO {
    private String param;
}
