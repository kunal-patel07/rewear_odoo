package com.example.cafe_backedn.services;

import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.config.ProductMapper;
import com.example.cafe_backedn.dto.ProductDto;
import com.example.cafe_backedn.entity.ProductEntity;
import com.example.cafe_backedn.repo.ProductRepo;
import com.example.cafe_backedn.repo.CategoryRepo;
import com.example.cafe_backedn.utils.PaginationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;
    private final ProductMapper productMapper;


    public List<ProductDto> getAllProduct() {
        List<ProductEntity> products = productRepo.findAllByOrderByIdDesc();
        if(products.isEmpty()) return null;
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto storeProduct(@Valid ProductDto data) {
        ProductEntity product = modelMapper.map(data,ProductEntity.class);
        product.setCategory(product.getCategory());
        ProductEntity create = productRepo.save(product);
        return modelMapper.map(create,ProductDto.class);
    }

    @Transactional
    public ProductDto updateProduct(Map<String, Object> data, Long id) {
        ProductEntity product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if (data.containsKey("name")) {
            product.setName((String) data.get("name"));
        }
        if (data.containsKey("price")) {
            Object priceValue = data.get("price");
            if (priceValue instanceof Integer) {
                product.setPrice((Integer) priceValue);
            } else if (priceValue instanceof String) {
                product.setPrice(Integer.parseInt((String) priceValue));
            }
        }
        if (data.containsKey("description")) {
            product.setDescription((String) data.get("description"));
        }
        if (data.containsKey("category")) {
            Object categoryValue = data.get("category");
            Long categoryId;
            
            // Convert category value to Long
            if (categoryValue instanceof Integer) {
                categoryId = ((Integer) categoryValue).longValue();
            } else if (categoryValue instanceof String) {
                categoryId = Long.parseLong((String) categoryValue);
            } else if (categoryValue instanceof Long) {
                categoryId = (Long) categoryValue;
            } else {
                throw new RuntimeException("Invalid category value type");
            }

            // Verify category exists
            if (!categoryRepo.existsById(categoryId)) {
                throw new RuntimeException("Category not found with ID: " + categoryId);
            }
            
            product.setCategory(categoryId.intValue());
        }

        product.setUpdatedAt(new Date());
        ProductEntity updated = productRepo.save(product);
        return productMapper.toDto(updated);
    }


    public ProductDto getProductById(Long id) {
        ProductEntity product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        return modelMapper.map(product,ProductDto.class);
    }

    public boolean isProductExist(Long id) {
        return productRepo.existsById(id);
    }

    public boolean deleteProduct(Long id) {
        productRepo.deleteById(id);
        return true;
    }

    public PaginationResponse<ProductDto> getPaginatedProducts(Pageable pageable,String keyword) {
        Page<ProductEntity> page;

        if (keyword == null || keyword.trim().isEmpty()) {
            page = productRepo.findAll(pageable);
        } else {
            page = productRepo.findByNameContainingIgnoreCase(keyword, pageable);
        }

        Page<ProductDto> dtoPage = page.map(productMapper::toDto);
        return PaginationUtil.buildPageResponse(dtoPage);
    }

    public List<ProductDto> getByCategoryId(Long id) {
        List<ProductEntity> products = productRepo.findByCategory(id);
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public Object getCountCategory() {
        String cont = String.valueOf(productRepo.count());
        return cont;
    }
}
