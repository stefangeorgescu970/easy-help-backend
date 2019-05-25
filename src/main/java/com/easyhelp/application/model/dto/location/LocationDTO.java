package com.easyhelp.application.model.dto.location;

import com.easyhelp.application.model.dto.BaseDTO;
import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.locations.RealLocation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationDTO extends BaseDTO {
    private Long id;
    private String name;
    private String address;
    private County county;
    private double longitude;
    private double latitude;

    public LocationDTO(RealLocation location) {
        id = location.getId();
        name = location.getName();
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        address = location.getAddress();
        county = location.getCounty();
    }
}
