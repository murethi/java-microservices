package com.programming.techie.productservice.controller;

import com.programming.techie.productservice.dto.ProductRequest;
import com.programming.techie.productservice.dto.ProductResponse;
import com.programming.techie.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> index(){
        return productService.getAllProducts();
    }
}
