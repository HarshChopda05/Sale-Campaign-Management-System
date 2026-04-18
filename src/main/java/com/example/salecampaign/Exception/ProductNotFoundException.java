package com.example.salecampaign.Exception;

import com.example.salecampaign.Service.ProductService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message){
        super(message);
    }
}
