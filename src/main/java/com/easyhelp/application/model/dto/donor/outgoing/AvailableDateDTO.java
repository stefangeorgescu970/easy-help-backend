package com.easyhelp.application.model.dto.donor.outgoing;

import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Data
public class AvailableDateDTO extends BaseOutgoingDTO {
    private String date;
    private List<String> availableHours;

    public AvailableDateDTO(AvailableDate availableDate) {
        this.date = availableDate.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.availableHours = availableDate.getAvailableHours().stream().map(availableDateInt -> availableDateInt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).collect(Collectors.toList());
    }
}
