package com.easyhelp.application.model.dto.donor.outgoing;

import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Data
public class AvailableDateDTO extends BaseOutgoingDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private List<ZonedDateTime> availableHours;

    public AvailableDateDTO(AvailableDate availableDate) {
        this.date = availableDate.getDate();
        this.availableHours = availableDate.getAvailableHours();
    }
}
