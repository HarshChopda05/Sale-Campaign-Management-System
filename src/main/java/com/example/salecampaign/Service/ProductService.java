package com.example.salecampaign.Service;

import com.example.salecampaign.DTOs.ProductAddRequestDTO;
import com.example.salecampaign.DTOs.ProductPaginationResponseDTO;
import com.example.salecampaign.DTOs.ProductRequestDTO;
import com.example.salecampaign.DTOs.PaginationProductResponseDTO;
import com.example.salecampaign.Exception.InvalidInventoryException;
import com.example.salecampaign.Exception.InvalidMRPException;
import com.example.salecampaign.Exception.ProductNotFoundException;
import com.example.salecampaign.Model.Product;
import com.example.salecampaign.Model.ProductHistory;
import com.example.salecampaign.Repository.ProductHistoryRepository;
import com.example.salecampaign.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductHistoryRepository productHistoryRepository;


    public void addProducts(ProductAddRequestDTO productAddRequestDTO) {

            //Avoid crash if request is empty
            if (productAddRequestDTO == null ||
                    productAddRequestDTO.getProductRequestDTOS() == null ||
                    productAddRequestDTO.getProductRequestDTOS().isEmpty()) {
                    throw new ProductNotFoundException("Product list cannot be empty");
            }

        List<Product> productList = new ArrayList<>();

        //ProductList
        for (ProductRequestDTO productRequestDTO : productAddRequestDTO.getProductRequestDTOS()){

                if (productRequestDTO.getMrp() == null || productRequestDTO.getMrp() <= 0) {
                    throw new InvalidMRPException("Invalid MRP");
                }

                if (productRequestDTO.getInventory() == null || productRequestDTO.getInventory() < 0) {
                    throw new InvalidInventoryException("Invalid Inventory");
                }

            Product product = new Product();
            product.setProductName(productRequestDTO.getProductName());
            product.setMrp(productRequestDTO.getMrp());
            product.setInventory(productRequestDTO.getInventory());
            //Set current price automatically same as MRP
            product.setCurrentPrice(productRequestDTO.getMrp()); //currentPrice = MRP, because first initial price is same as real price

            productList.add(product);

        }
        //save All products.
        List<Product> saveProducts = productRepository.saveAll(productList);

        //create productHistory List
        List<ProductHistory> historyList = new ArrayList<>();

        for (Product product : saveProducts){

            //Add initial Price to the ProductHistory table
            ProductHistory productHistory = new ProductHistory();
            productHistory.setProduct(product);
            productHistory.setPrice(product.getCurrentPrice());
            productHistory.setDate(LocalDate.now()); //Date of current day of changes

            historyList.add(productHistory); //add history object to the list

        }

        productHistoryRepository.saveAll(historyList);
    }

    //For pageable
    public ProductPaginationResponseDTO getAllProducts(int page, int pageSize) {

        Page<Product> productPage = productRepository.findAll(PageRequest.of(page - 1, pageSize));
        int totalPages = productPage.getTotalPages();

        PaginationProductResponseDTO[] paginationProductResponseDTO = new PaginationProductResponseDTO[productPage.getNumberOfElements()];

        int i = 0;
        for (Product product : productPage) {
            int id = product.getProductId();
            double mrp = product.getMrp();
            double currentPrice = product.getCurrentPrice();

            //Calculate discount (if not stored in DB)
            double discount = ((mrp - currentPrice) / mrp) * 100;
            int inventory = product.getInventory();
            paginationProductResponseDTO[i++] =
                    new PaginationProductResponseDTO(id, mrp, currentPrice, (int) discount, inventory);
        }
        //FINAL RETURN
        return new ProductPaginationResponseDTO(Arrays.asList(paginationProductResponseDTO), page, pageSize, totalPages);

    }
    //All products and their information including current price adjusted with any ongoing sale
    public List<PaginationProductResponseDTO> getProductsWithDiscount() {

        List<Object[]> results = productRepository.getProductsWithDiscount();
        List<PaginationProductResponseDTO> response = new ArrayList<>();

        for (Object[] row : results) {

            Integer id = ((Number) row[0]).intValue();
            String name = (String) row[1];
            Double mrp = ((Number) row[2]).doubleValue();
            Double basePrice = row[3] != null
                    ? ((Number) row[3]).doubleValue()
                    : 0.0;
            Integer discount = row[4] != null ? ((Number) row[4]).intValue() : 0;
            Integer inventory = ((Number) row[5]).intValue();

            PaginationProductResponseDTO dto =
                    new PaginationProductResponseDTO(id, mrp, basePrice, discount, inventory);

            response.add(dto);
        }
        return response;
    }
}
