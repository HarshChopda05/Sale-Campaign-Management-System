package com.example.salecampaign.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> ProductNotFoundExceptionHandler(ProductNotFoundException e){

        ErrorResponse error =
                new ErrorResponse(LocalDateTime.now(),HttpStatus.NOT_FOUND.value(), "NOT_FOUND", e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DiscountException.class)
    public ResponseEntity<ErrorResponse> DiscountExceptionHandler(DiscountException e){

        ErrorResponse error =
                new ErrorResponse(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(), "DISCOUNT_ERROR", e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDate(InvalidDateException ex) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_DATE",
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidMRPException.class)
    public ResponseEntity<ErrorResponse> InvalidMRPExceptionHandler(InvalidMRPException e){
        ErrorResponse error =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "INVALID_MRP", e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInventoryException.class)
    public ResponseEntity<ErrorResponse> handleInventory(InvalidInventoryException ex) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_INVENTORY",
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Generic Exception (VERY IMPORTANT)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(Exception ex) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
