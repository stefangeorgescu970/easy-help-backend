package com.easyhelp.application.model.donations;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class AvailableDate {

    private Date date;
    private List<Date> availableHours;

    public AvailableDate(Date date, List<Date> availableHours) {
        this.date = date;
        this.availableHours = availableHours;
    }
}
