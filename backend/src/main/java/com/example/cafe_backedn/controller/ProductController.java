package com.example.cafe_backedn.controller;

import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.dto.ProductDto;
import com.example.cafe_backedn.services.CategoryService;
import com.example.cafe_backedn.services.ProductService;
import com.example.cafe_backedn.utils.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllCategory(){
        List<ProductDto> allProduct = productService.getAllProduct();
        if(allProduct == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allProduct);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDto> storeProduct(@Valid @RequestBody ProductDto data){
        ProductDto productDto = productService.storeProduct(data);
        return ResponseEntity.ok(productDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getOneProduct(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody Map<String, Object> data, @PathVariable Long id) {
        try {
            ProductDto updated = productService.updateProduct(data, id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteProduct(@PathVariable Long id){
        return productService.deleteProduct(id);
    }

    @GetMapping("/products")
    public ResponseEntity<PaginationResponse<ProductDto>> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        PaginationResponse<ProductDto> response = productService.getPaginatedProducts(pageable,keyword);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/count")
    public Object getProductCount(){
        Map<String, Object> response = new HashMap<>();
        response.put("count", productService.getCountCategory());
        return response;
    }
    @GetMapping("/getByCategory/{id}")
    public ResponseEntity<List<ProductDto>> getByCategoryId(
            @PathVariable Long id
    ){
        List<ProductDto> allProduct = productService.getByCategoryId(id);
        return ResponseEntity.ok(allProduct);
    }

}
