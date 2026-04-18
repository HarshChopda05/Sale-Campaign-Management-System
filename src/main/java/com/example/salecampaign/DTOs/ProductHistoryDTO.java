package com.example.salecampaign.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductHistoryDTO {

    private Integer productId;
    private Double price;
    private LocalDate date;
}
