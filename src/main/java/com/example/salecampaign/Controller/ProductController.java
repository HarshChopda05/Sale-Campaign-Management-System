package com.example.salecampaign.Controller;

import com.example.salecampaign.DTOs.PaginationProductResponseDTO;
import com.example.salecampaign.DTOs.ProductAddRequestDTO;
import com.example.salecampaign.DTOs.ProductHistoryDTO;
import com.example.salecampaign.DTOs.ProductPaginationResponseDTO;
import com.example.salecampaign.Service.CampaignService;
import com.example.salecampaign.Service.ProductHistoryService;
import com.example.salecampaign.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductHistoryService productHistoryService;

    @Autowired
    CampaignService campaignService;

    @PostMapping("/add-products") //here add try catch because if multiple causes are occur then it will notify
    public ResponseEntity<?> addProducts(@Valid @RequestBody ProductAddRequestDTO productAddRequestDTO){

        if(productAddRequestDTO == null || productAddRequestDTO.getProductRequestDTOS().isEmpty()){
            throw new IllegalArgumentException("Product list cannot be empty");
        }
        productService.addProducts(productAddRequestDTO);
        return ResponseEntity.status(201).body("Products added successfully");
    }

    //Get Products
    @GetMapping("/get-products-pagination")
    public ResponseEntity<ProductPaginationResponseDTO> getProducts(
                        @RequestParam(name = "page", defaultValue = "0") int pageNo,
                        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        return new ResponseEntity<>(productService.getAllProducts(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/products-with-discount")
    public ResponseEntity<List<PaginationProductResponseDTO>> getProducts() {
        return new ResponseEntity<>(productService.getProductsWithDiscount(), HttpStatus.OK);
    }

    @GetMapping("/price-history-each-product")
    public ResponseEntity<List<ProductHistoryDTO>> getHistory(){
        return new ResponseEntity<>(productHistoryService.getHistory(), HttpStatus.OK);
    }

}
