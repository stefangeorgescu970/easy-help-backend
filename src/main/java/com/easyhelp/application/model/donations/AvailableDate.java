package com.easyhelp.application.model.donations;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class AvailableDate {
    private ZonedDateTime date;
    private List<ZonedDateTime> availableHours;

    public AvailableDate(ZonedDateTime date, List<ZonedDateTime> availableHours) {
        this.date = date;
        this.availableHours = availableHours;
    }
}
