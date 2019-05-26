package com.easyhelp.application.model.dto.misc.outgoing;

import com.easyhelp.application.model.dto.BaseOutgoingDTO;
import com.easyhelp.application.model.locations.County;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class EnumsDTO extends BaseOutgoingDTO {

    private List<String> counties;

    public EnumsDTO(){
        counties = asList(County.values());
    }

    private List<String> asList(Enum[] values) {
        return Arrays.stream(values)
                .map(Enum::toString)
                .collect(Collectors.toList());
    }
}
