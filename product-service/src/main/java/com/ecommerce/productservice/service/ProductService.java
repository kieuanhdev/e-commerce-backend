package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    void createProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id); // ID là Long
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long id);
}