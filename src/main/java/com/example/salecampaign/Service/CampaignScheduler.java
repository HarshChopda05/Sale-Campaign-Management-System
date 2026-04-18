package com.example.salecampaign.Service;

import com.example.salecampaign.CampaignStatus;
import com.example.salecampaign.Exception.InvalidDateException;
import com.example.salecampaign.Model.Campaign;
import com.example.salecampaign.Model.CampaignProduct;
import com.example.salecampaign.Model.Product;
import com.example.salecampaign.Model.ProductHistory;
import com.example.salecampaign.Repository.CampaignProductRepository;
import com.example.salecampaign.Repository.CampaignRepository;
import com.example.salecampaign.Repository.ProductHistoryRepository;
import com.example.salecampaign.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class CampaignScheduler {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CampaignRepository campaignRepository;
    @Autowired
    CampaignProductRepository campaignProductRepository;
    @Autowired
    ProductHistoryRepository productHistoryRepository;

    //every night 12AM : "0 0 0 * * ?"
    //every 10 seconds: */10 * * * * *

    @Scheduled(cron = "*/10 * * * * ?") //every 1 minute
    @Transactional
    public void runScheduler() {
        LocalDate currentDate = LocalDate.now();

        //Use Set to avoid duplicate products
        Set<Product> productsToUpdate = new HashSet<>();

        // Get all campaigns ONCE
        List<Campaign> campaigns = campaignRepository.findAll();

        for (Campaign campaign : campaigns) {
            String oldStatus = campaign.getStatus();
            String newStatus;

            if (currentDate.isBefore(campaign.getStartDate())) {
                newStatus = "UPCOMING";
            } else if (currentDate.isAfter(campaign.getEndDate())) {
                newStatus = "EXPIRED";
            } else {
                newStatus = "CURRENT";
            }

            // Update only if status changed
            if (oldStatus == null || !oldStatus.equals(newStatus)) {
                campaign.setStatus(newStatus);
                campaignRepository.save(campaign);

                // collect related products
                List<CampaignProduct> campaignProducts = campaignProductRepository.findByCampaign(campaign);

                for (CampaignProduct cp : campaignProducts) {
                    productsToUpdate.add(cp.getProduct());
                }
            }
        }

        // Update each product ONLY ONCE
        for (Product product : productsToUpdate) {
            updateProductPrice(product);
        }
    }

        // STATUS LOGIC
        private String getCampaignStatus(LocalDate startDate, LocalDate endDate) {


                // Null validation
                if (startDate == null || endDate == null) {
                    throw new InvalidDateException("Start date and End date cannot be null");
                }

                // Logical validation
                if (startDate.isAfter(endDate)) {
                    throw new InvalidDateException("Start date must be before or equal to End date");
                }

                LocalDate today = LocalDate.now();

                // CURRENT
                if ((today.isEqual(startDate) || today.isAfter(startDate)) &&
                        (today.isEqual(endDate) || today.isBefore(endDate))) {
                    return CampaignStatus.CURRENT.name();
                }

                // UPCOMING
                else if (today.isBefore(startDate)) {
                    return CampaignStatus.UPCOMING.name();
                }

                // PAST
                else {
                    return CampaignStatus.PAST.name();
                }
        }

        public void updateProductPrice (Product product){

            LocalDate currentDate = LocalDate.now();

            //get active campaigns for this product, and change it's price according it's discount
            List<CampaignProduct> activeCampaigns = campaignProductRepository.findActiveCampaignsByProduct(product.getProductId(), currentDate);

            //calculate final price here,
            //sort here, Correct pricing order, Order matters when multiple discounts apply
            activeCampaigns.sort(Comparator.comparing(cp -> cp.getCampaign().getStartDate()));

            //If any changes in a price, only change it
            Double oldPrice = product.getCurrentPrice();
            Double newPrice;
            if (activeCampaigns.isEmpty()) { //Revert Campaign's product price when campaign is expired.
                newPrice = product.getMrp();
            } else {
                Double price = product.getMrp();
                for (CampaignProduct cp : activeCampaigns) {
                    price = price - (price * cp.getDiscount() / 100);  //Applies multiple campaigns sequentially, This satisfies: Multiple campaigns on same product
                }
                newPrice = price;
            }

            //Only update if price changed
            if (!oldPrice.equals(newPrice)) {

                //Save Old Price to ProductHistory table.
                ProductHistory oldHistory = new ProductHistory();
                oldHistory.setProduct(product);
                oldHistory.setPrice(product.getCurrentPrice());
                oldHistory.setDate(LocalDate.now());
                productHistoryRepository.save(oldHistory);

                //save update
                product.setCurrentPrice(newPrice);
                productRepository.save(product);

                //Save Discounted new price to ProductHistory table.
                ProductHistory newHistory = new ProductHistory();
                newHistory.setProduct(product);
                newHistory.setPrice(product.getCurrentPrice());
                newHistory.setDate(LocalDate.now());
                productHistoryRepository.save(newHistory);

                System.out.println("Updated Product ID: " + product.getProductId() +
                        " | Old Price: " + oldPrice +
                        " | New Price: " + newPrice);
            }
        }
}

