package com.easyhelp.application.model.dto.location;

import com.easyhelp.application.model.locations.County;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class EnumsDto implements Serializable {

    private List<String> counties;

    public EnumsDto(){
        counties = asList(County.values());
    }

    private List<String> asList(Enum[] values) {
        return Arrays.stream(values)
                .map(Enum::toString)
                .collect(Collectors.toList());
    }
}
