package com.ecommerce.reviewservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_reviews")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skuCode; // Mã sản phẩm
    private String userId;  // ID người đánh giá (Lấy từ Token)
    private Double rating;  // Điểm sao (1-5)
    private String comment; // Lời bình
    private LocalDateTime createdAt; // Thời gian tạo
}