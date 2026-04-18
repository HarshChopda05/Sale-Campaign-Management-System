package com.example.salecampaign.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDTO {

    private Integer campaignId;
    private String campaignName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
