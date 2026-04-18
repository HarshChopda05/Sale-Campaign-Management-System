package com.example.salecampaign.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequestDTO {

    private String productName;
    private Double mrp;
    private Integer inventory;
}
