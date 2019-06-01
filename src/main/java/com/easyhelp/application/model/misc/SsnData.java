package com.easyhelp.application.model.misc;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class SsnData {
    private Boolean isMale;
    private LocalDate dateOfBirth;
}
