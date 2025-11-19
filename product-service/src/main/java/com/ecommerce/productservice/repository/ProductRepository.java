package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// Đổi <Product, Long> thành <Product, String>
public interface ProductRepository extends JpaRepository<Product, String> {
}