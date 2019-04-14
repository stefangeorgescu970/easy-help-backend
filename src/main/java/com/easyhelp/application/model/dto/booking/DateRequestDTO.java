package com.easyhelp.application.model.dto.booking;

import com.easyhelp.application.model.dto.misc.IdentifierDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DateRequestDTO extends IdentifierDTO {

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date selectedDate;

}