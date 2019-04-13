package com.easyhelp.application.model.dto.booking;

import com.easyhelp.application.model.dto.misc.IdentifierDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class BookingRequestDTO extends IdentifierDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Calendar selectedDate;
    private Long donationCenterId;
}

