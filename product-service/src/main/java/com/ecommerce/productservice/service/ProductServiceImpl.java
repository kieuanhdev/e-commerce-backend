package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .isVisible(productRequest.getIsVisible() != null ? productRequest.getIsVisible() : true)
                .lowStockThreshold(productRequest.getLowStockThreshold() != null ? productRequest.getLowStockThreshold() : 10)
                .imageUrl(productRequest.getImageUrl())
                .shortDescription(productRequest.getShortDescription())
                .longDescription(productRequest.getLongDescription())
                .categoryId(productRequest.getCategoryId())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
        // 👇 ĐÃ SỬA: Trả về dữ liệu ProductResponse thay vì void (không trả gì)
        return mapToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(String id) { // Đổi Long thành String
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found id: " + id));
        return mapToProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(String id, ProductRequest request) { // Đổi Long thành String
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found id: " + id));

        // Cập nhật tất cả các trường mới
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setIsVisible(request.getIsVisible());
        product.setLowStockThreshold(request.getLowStockThreshold());
        product.setImageUrl(request.getImageUrl());
        product.setShortDescription(request.getShortDescription());
        product.setLongDescription(request.getLongDescription());
        product.setCategoryId(request.getCategoryId());

        productRepository.save(product);
        return mapToProductResponse(product);
    }

    @Override
    public void deleteProduct(String id) { // Đổi Long thành String
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .isVisible(product.getIsVisible())
                .lowStockThreshold(product.getLowStockThreshold())
                .imageUrl(product.getImageUrl())
                .shortDescription(product.getShortDescription())
                .longDescription(product.getLongDescription())
                .categoryId(product.getCategoryId())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}