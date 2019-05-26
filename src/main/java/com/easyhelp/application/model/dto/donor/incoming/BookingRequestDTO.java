package com.easyhelp.application.model.dto.donor.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.easyhelp.application.model.dto.misc.outgoing.BaseOutgoingLocationDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BookingRequestDTO extends BaseIncomingDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date selectedDate;
    private Long donationCenterId;
    private String patientSSN;
}

