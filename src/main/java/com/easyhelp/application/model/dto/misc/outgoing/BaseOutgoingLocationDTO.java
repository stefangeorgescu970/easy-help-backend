package com.easyhelp.application.model.dto.misc.outgoing;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.locations.RealLocation;
import lombok.Data;

@Data
public class BaseOutgoingLocationDTO extends BaseOutgoingDTO {
    private Long id;
    private String name;

    public BaseOutgoingLocationDTO(RealLocation location) {
        id = location.getId();
        name = location.getName();
    }
}
