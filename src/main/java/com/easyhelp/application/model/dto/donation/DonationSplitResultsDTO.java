package com.easyhelp.application.model.dto.donation;

import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

@Data
public class DonationSplitResultsDTO extends BaseDTO {
    private Long donationId;

    private double plateletsUnits;
    private double redBloodCellsUnits;
    private double plasmaUnits;
}
