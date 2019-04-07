package com.easyhelp.application.model.dto.booking;


import com.easyhelp.application.model.dto.BaseDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AvailableDate extends BaseDTO {

    private Date date;
    private List<Date> availableHours;

    public AvailableDate(Date date, List<Date> availableHours) {
        this.date = date;
        this.availableHours = availableHours;
    }
}
