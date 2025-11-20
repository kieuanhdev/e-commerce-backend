package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(String id); // ID là String
    ProductResponse updateProduct(String id, ProductRequest productRequest); // ID là String
    void deleteProduct(String id); // ID là String
}