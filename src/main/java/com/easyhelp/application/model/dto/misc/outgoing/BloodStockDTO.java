package com.easyhelp.application.model.dto.misc.outgoing;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;

@Data
public class BloodStockDTO extends BaseOutgoingDTO {
    private BloodComponent component;
    private Double amount;
}
