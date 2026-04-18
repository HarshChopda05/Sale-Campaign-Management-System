package com.example.salecampaign.DTOs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data

public class CampaignProductRequestDTO {

    private Integer productId;
    private Integer discount;

}
