package com.easyhelp.application.model.donations;

import com.easyhelp.application.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonorSummary extends BaseEntity {
    private Integer donationsNumber;
    private Donation lastDonation;
    private DonationBooking nextBooking;
}
