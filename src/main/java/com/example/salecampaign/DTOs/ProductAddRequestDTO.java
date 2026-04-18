package com.example.salecampaign.DTOs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data

public class ProductAddRequestDTO {

    private List<ProductRequestDTO> productRequestDTOS;
}
