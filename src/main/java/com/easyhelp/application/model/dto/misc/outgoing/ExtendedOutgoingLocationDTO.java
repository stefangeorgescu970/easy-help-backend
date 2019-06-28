package com.easyhelp.application.model.dto.misc.outgoing;

import com.easyhelp.application.model.locations.County;
import com.easyhelp.application.model.locations.RealLocation;
import lombok.Data;

@Data
public class ExtendedOutgoingLocationDTO extends BaseOutgoingLocationDTO {
    private String address;
    private County county;
    private Double longitude;
    private Double latitude;
    private String phone;

    public ExtendedOutgoingLocationDTO(RealLocation location) {
        super(location);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        address = location.getAddress();
        county = location.getCounty();
        phone = location.getPhone();
    }
}
