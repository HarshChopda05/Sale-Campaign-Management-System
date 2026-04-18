package com.example.salecampaign.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginationProductResponseDTO {

    private Integer id;
    private Double mrp;
    private Double currentPrice;
    private Integer discount;
    private Integer inventory;
}
