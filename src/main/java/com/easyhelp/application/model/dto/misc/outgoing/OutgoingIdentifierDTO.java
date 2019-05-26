package com.easyhelp.application.model.dto.misc.outgoing;

import com.easyhelp.application.model.BaseEntity;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;

@Data
public class OutgoingIdentifierDTO extends BaseOutgoingDTO {
    private Long newId;

    public OutgoingIdentifierDTO(BaseEntity entity) {
        newId = entity.getId();
    }
}
