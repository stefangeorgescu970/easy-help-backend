package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DonationCreationDTO extends BaseDTO {

    private Long bookingId;
    private String groupLetter;
    private Boolean rh;

}
