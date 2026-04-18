package com.example.salecampaign.DTOs;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductPaginationResponseDTO {

    private List<PaginationProductResponseDTO> products;
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
}

