package com.easyhelp.application.model.dto.booking;

import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AvailableDateDTO extends BaseDTO {
    private String date;
    private List<String> availableHours;

    public AvailableDateDTO(AvailableDate availableDate) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        this.date = dateFormat.format(availableDate.getDate());
        this.availableHours = availableDate.getAvailableHours().stream().map(dateFormat::format).collect(Collectors.toList());
    }
}
