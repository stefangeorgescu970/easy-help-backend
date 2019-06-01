package com.easyhelp.application.model.dto.donor.incoming;

import com.easyhelp.application.model.dto.BaseIncomingDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDTO extends BaseIncomingDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime selectedDate;
    private Long donationCenterId;
    private String patientSSN;
}

