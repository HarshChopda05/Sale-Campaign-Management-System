package com.example.salecampaign.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CampaignAddRequestDTO {

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CampaignProductRequestDTO> campaignDiscount;

}
