package com.ecommerce.productservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class) // Để tự động điền ngày giờ
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Tự sinh ID dạng chuỗi ngẫu nhiên
    private String id;

    private String name;
    private BigDecimal price; // Backend nên dùng BigDecimal cho tiền tệ

    @Column(columnDefinition = "boolean default true")
    private Boolean isVisible;

    private Integer quantity; // Lưu ý: Trong microservices chuẩn, cái này nên ở Inventory, nhưng để giống FE tôi để tạm ở đây.
    private Integer lowStockThreshold;

    private String imageUrl;
    private String shortDescription;

    @Column(length = 5000) // Cho phép mô tả dài
    private String longDescription;

    private String categoryId;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}