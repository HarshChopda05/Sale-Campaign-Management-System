package com.example.salecampaign.Service;

import com.example.salecampaign.DTOs.CampaignDTO;
import com.example.salecampaign.DTOs.CampaignProductRequestDTO;
import com.example.salecampaign.DTOs.CampaignAddRequestDTO;
import com.example.salecampaign.Exception.DiscountException;
import com.example.salecampaign.Exception.InvalidDateException;
import com.example.salecampaign.Exception.ProductNotFoundException;
import com.example.salecampaign.Model.Campaign;
import com.example.salecampaign.Model.CampaignProduct;
import com.example.salecampaign.Model.Product;
import com.example.salecampaign.Repository.CampaignProductRepository;
import com.example.salecampaign.Repository.CampaignRepository;
import com.example.salecampaign.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class CampaignService {

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CampaignProductRepository campaignProductRepository;

    @Transactional
    public void addCampaign(CampaignAddRequestDTO campaignAddRequestDTO) {

        String campaignName = campaignAddRequestDTO.getTitle();
        LocalDate startDate = campaignAddRequestDTO.getStartDate();
        LocalDate endDate = campaignAddRequestDTO.getEndDate();

        if (campaignName == null || campaignName.isEmpty()){
            throw new IllegalArgumentException("Campaign name is required!");
        }

        // Validation: status
        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and End date cannot be null");
        }

        if (endDate.isBefore(startDate)) {
            throw new InvalidDateException("End date must be after start date");
        }

        //save campaign
        Campaign campaign = new Campaign();
        campaign.setCampaignName(campaignAddRequestDTO.getTitle());
        campaign.setStartDate(campaignAddRequestDTO.getStartDate());
        campaign.setEndDate(campaignAddRequestDTO.getEndDate());

        campaignRepository.save(campaign);

        //save campaign product
        if (campaignAddRequestDTO.getCampaignDiscount() != null) {
            for (CampaignProductRequestDTO campaignProductRequestDTO : campaignAddRequestDTO.getCampaignDiscount()) {

                Product product = productRepository.findById(campaignProductRequestDTO.getProductId())
                        .orElseThrow(() -> new ProductNotFoundException("Product is not found!"));

                    Integer discount = campaignProductRequestDTO.getDiscount();
                    if (discount == null){
                        throw new DiscountException("Discount can not be null");
                    }
                    if (discount <= 0 || discount > 100) {
                        throw new DiscountException("Discount must be between 1 and 100");
                    }

                CampaignProduct campaignProduct = new CampaignProduct();
                campaignProduct.setCampaign(campaign); //here set Campaign to CampaignProduct Table
                campaignProduct.setProduct(product);
                campaignProduct.setDiscount(campaignProductRequestDTO.getDiscount());

                campaignProductRepository.save(campaignProduct);
            }
        }
    }
    public List<CampaignDTO> getCampaigns() {

        List<Object[]> results = campaignRepository.getAllCampaigns();
        List<CampaignDTO> list = new ArrayList<>();

        for (Object[] row : results){
            Integer id = ((Number) row[0]).intValue();
            String name = (String) row[1];

            LocalDate startDate = row[2] != null ? ((Date) row[2]).toLocalDate() : null;
            LocalDate endDate = row[3] != null ? ((Date) row[3]).toLocalDate() : null;
            String status = (String) row[4];
            list.add(new CampaignDTO(id, name, startDate, endDate, status));

        }
        return list;
    }
}
