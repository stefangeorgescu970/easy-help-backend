package com.easyhelp.application.model.donations;

import com.easyhelp.application.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonorSummary extends BaseEntity {
    private Integer donationsNumber;
    private String lastDonation;
    private String nextBooking;
}
