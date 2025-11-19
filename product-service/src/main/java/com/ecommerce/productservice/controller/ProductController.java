package com.ecommerce.productservice.controller;

// 👇 Chú ý: Package này phải khớp với package trong module common-library của bạn
// Nếu bạn để là com.ecommerce.common.response thì sửa lại nhé
import com.ecommerce.commonlibrary.response.ResponseData;
import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 1. Tạo mới
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseData<String> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Thêm sản phẩm thành công", null);
    }

    // 2. Lấy danh sách
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách thành công", products);
    }

    // 3. Lấy chi tiết (Đã sửa)
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<ProductResponse> getProductById(@PathVariable String id) {
        ProductResponse product = productService.getProductById(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy chi tiết sản phẩm thành công", product);
    }

    // 4. Cập nhật (Đã sửa)
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<ProductResponse> updateProduct(@PathVariable String id, @RequestBody @Valid ProductRequest productRequest) {
        ProductResponse product = productService.updateProduct(id, productRequest);
        return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật sản phẩm thành công", product);
    }

    // 5. Xóa (Đã sửa)
    // Lưu ý: Phải dùng HttpStatus.OK (200) thì mới trả về được JSON message.
    // Nếu dùng NO_CONTENT (204) thì Body sẽ bị rỗng.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Xóa sản phẩm thành công", null);
    }
}