package com.easyhelp.application.model.dto.donor.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class BookingRequestDTO extends BaseIncomingDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime selectedDate;
    private Long donationCenterId;
    private String patientSSN;
}

