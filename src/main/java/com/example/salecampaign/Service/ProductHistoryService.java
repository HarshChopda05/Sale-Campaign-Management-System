package com.example.salecampaign.Service;

import com.example.salecampaign.DTOs.ProductHistoryDTO;
import com.example.salecampaign.Repository.ProductHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductHistoryService {

    @Autowired
    ProductHistoryRepository productHistoryRepository;

    public List<ProductHistoryDTO> getHistory(){

        List<Object[]> results = productHistoryRepository.getProductHistory();
        List<ProductHistoryDTO> list = new ArrayList<>();

        for (Object[] row : results) {

            Integer productId = ((Number) row[0]).intValue();

            Double price = ((Number) row[1]).doubleValue();

            LocalDate date = row[2] != null ? ((Date) row[2]).toLocalDate() : null;

            list.add(new ProductHistoryDTO(productId, price, date));
        }

        return list;
    }
}
