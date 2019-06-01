package com.easyhelp.application.model.donations;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class AvailableDate {
    private LocalDateTime date;
    private List<LocalDateTime> availableHours;

    public AvailableDate(LocalDateTime date, List<LocalDateTime> availableHours) {
        this.date = date;
        this.availableHours = availableHours;
    }
}
