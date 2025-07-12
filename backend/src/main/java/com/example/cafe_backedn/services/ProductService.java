package com.example.cafe_backedn.services;

import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.config.ProductMapper;
import com.example.cafe_backedn.dto.ProductDto;
import com.example.cafe_backedn.dto.ProductImageDto;
import com.example.cafe_backedn.dto.UpdateProductDto;
import com.example.cafe_backedn.entity.ProductEntity;
import com.example.cafe_backedn.entity.ProductImageEntity;
import com.example.cafe_backedn.entity.CategoryEntity;
import com.example.cafe_backedn.entity.UserEntity;
import com.example.cafe_backedn.repo.ProductRepo;
import com.example.cafe_backedn.repo.ProductImageRepo;
import com.example.cafe_backedn.repo.CategoryRepo;
import com.example.cafe_backedn.repo.UserRepo;
import com.example.cafe_backedn.utils.PaginationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductImageRepo productImageRepo;
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final ProductMapper productMapper;

    // Helper method to manually map ProductEntity to ProductDto to avoid ModelMapper conflicts
    private ProductDto mapEntityToDto(ProductEntity entity) {
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setOriginalPrice(entity.getOriginalPrice());
        dto.setConditionType(entity.getConditionType());
        dto.setBrand(entity.getBrand());
        dto.setSize(entity.getSize());
        dto.setColor(entity.getColor());
        dto.setMaterial(entity.getMaterial());
        dto.setCategoryId(entity.getCategoryId());
        dto.setSellerId(entity.getSellerId());
        dto.setStatus(entity.getStatus());
        dto.setViews(entity.getViews());
        dto.setLikes(entity.getLikes());
        dto.setLocation(entity.getLocation());
        dto.setShippingCost(entity.getShippingCost());
        dto.setIsNegotiable(entity.getIsNegotiable());
        dto.setIsFeatured(entity.getIsFeatured());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        // Set category and seller names if entities exist
        if (entity.getCategory() != null) {
            dto.setCategoryName(entity.getCategory().getName());
        }
        if (entity.getSeller() != null) {
            dto.setSellerName(entity.getSeller().getName());
        }
        
        // Set images
        List<ProductImageEntity> imageEntities = productImageRepo.findByProductIdOrderBySortOrderAsc(entity.getId());
        List<ProductImageDto> imageDtos = imageEntities.stream()
                .map(this::mapImageEntityToDto)
                .collect(Collectors.toList());
        dto.setImages(imageDtos);
        
        return dto;
    }

    // Helper method to map ProductEntity to UpdateProductDto
    private UpdateProductDto mapEntityToUpdateDto(ProductEntity entity) {
        UpdateProductDto dto = new UpdateProductDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setOriginalPrice(entity.getOriginalPrice());
        dto.setConditionType(entity.getConditionType());
        dto.setBrand(entity.getBrand());
        dto.setSize(entity.getSize());
        dto.setColor(entity.getColor());
        dto.setMaterial(entity.getMaterial());
        dto.setCategoryId(entity.getCategoryId());
        dto.setStatus(entity.getStatus());
        dto.setViews(entity.getViews());
        dto.setLikes(entity.getLikes());
        dto.setLocation(entity.getLocation());
        dto.setShippingCost(entity.getShippingCost());
        dto.setIsNegotiable(entity.getIsNegotiable());
        dto.setIsFeatured(entity.getIsFeatured());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        // Set category name if entity exists
        if (entity.getCategory() != null) {
            dto.setCategoryName(entity.getCategory().getName());
        }
        
        // Set images
        List<ProductImageEntity> imageEntities = productImageRepo.findByProductIdOrderBySortOrderAsc(entity.getId());
        List<ProductImageDto> imageDtos = imageEntities.stream()
                .map(this::mapImageEntityToDto)
                .collect(Collectors.toList());
        dto.setImages(imageDtos);
        
        return dto;
    }

    // Helper method to map ProductImageEntity to ProductImageDto
    private ProductImageDto mapImageEntityToDto(ProductImageEntity entity) {
        ProductImageDto dto = new ProductImageDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProductId());
        dto.setImageUrl(entity.getImageUrl());
        dto.setAltText(entity.getAltText());
        dto.setSortOrder(entity.getSortOrder());
        dto.setIsPrimary(entity.getIsPrimary());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public List<ProductDto> getAllProduct() {
        List<ProductEntity> products = productRepo.findAllByOrderByIdDesc();
        if(products.isEmpty()) return null;
        return products.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Transactional
    public ProductDto createProduct(@Valid ProductDto data, List<String> imageUrls) {
        // Manual mapping to avoid ModelMapper conflicts
        ProductEntity product = new ProductEntity();
        product.setTitle(data.getTitle());
        product.setDescription(data.getDescription());
        product.setPrice(data.getPrice());
        product.setOriginalPrice(data.getOriginalPrice());
        product.setConditionType(data.getConditionType());
        product.setBrand(data.getBrand());
        product.setSize(data.getSize());
        product.setColor(data.getColor());
        product.setMaterial(data.getMaterial());
        product.setCategoryId(data.getCategoryId());
        product.setSellerId(data.getSellerId());
        
        // Set default values for fields that might be null from DTO
        product.setStatus(data.getStatus() != null ? data.getStatus() : ProductEntity.Status.active);
        product.setViews(data.getViews() != null ? data.getViews() : 0);
        product.setLikes(data.getLikes() != null ? data.getLikes() : 0);
        product.setLocation(data.getLocation());
        product.setShippingCost(data.getShippingCost() != null ? data.getShippingCost() : BigDecimal.valueOf(0.00));
        product.setIsNegotiable(data.getIsNegotiable() != null ? data.getIsNegotiable() : true);
        product.setIsFeatured(data.getIsFeatured() != null ? data.getIsFeatured() : false);
        
        // Debug log
        System.out.println("Creating product: " + data.getTitle());
        
        ProductEntity savedProduct = productRepo.save(product);
        
        // Save images
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (int i = 0; i < imageUrls.size(); i++) {
                ProductImageEntity image = new ProductImageEntity();
                image.setProductId(savedProduct.getId());
                image.setImageUrl(imageUrls.get(i));
                image.setSortOrder(i);
                image.setIsPrimary(i == 0); // First image is primary
                productImageRepo.save(image);
                
                System.out.println("Saved image " + (i + 1) + ": " + imageUrls.get(i));
            }
        }
        
        System.out.println("Product created with ID: " + savedProduct.getId());
        
        return mapEntityToDto(savedProduct);
    }

    @Transactional
    public UpdateProductDto updateProduct(@Valid UpdateProductDto data, Long id, List<String> newImageUrls) {
        ProductEntity product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        // Update fields
        product.setTitle(data.getTitle());
        product.setDescription(data.getDescription());
        product.setPrice(data.getPrice());
        product.setOriginalPrice(data.getOriginalPrice());
        product.setConditionType(data.getConditionType());
        product.setBrand(data.getBrand());
        product.setSize(data.getSize());
        product.setColor(data.getColor());
        product.setMaterial(data.getMaterial());
        product.setCategoryId(data.getCategoryId());
        
        if (data.getStatus() != null) {
            product.setStatus(data.getStatus());
        }
        if (data.getViews() != null) {
            product.setViews(data.getViews());
        }
        if (data.getLikes() != null) {
            product.setLikes(data.getLikes());
        }
        if (data.getLocation() != null) {
            product.setLocation(data.getLocation());
        }
        if (data.getShippingCost() != null) {
            product.setShippingCost(data.getShippingCost());
        }
        if (data.getIsNegotiable() != null) {
            product.setIsNegotiable(data.getIsNegotiable());
        }
        if (data.getIsFeatured() != null) {
            product.setIsFeatured(data.getIsFeatured());
        }

        // If new images are provided, add them (don't delete existing ones)
        if (newImageUrls != null && !newImageUrls.isEmpty()) {
            // Get current highest sort order
            List<ProductImageEntity> existingImages = productImageRepo.findByProductIdOrderBySortOrderAsc(id);
            int nextSortOrder = existingImages.size();
            
            for (String imageUrl : newImageUrls) {
                ProductImageEntity image = new ProductImageEntity();
                image.setProductId(id);
                image.setImageUrl(imageUrl);
                image.setSortOrder(nextSortOrder++);
                image.setIsPrimary(existingImages.isEmpty() && nextSortOrder == 1); // First image is primary if no existing images
                productImageRepo.save(image);
                
                System.out.println("Added new image: " + imageUrl);
            }
        }

        ProductEntity updatedProduct = productRepo.save(product);
        
        System.out.println("Product updated with ID: " + updatedProduct.getId());
        
        return mapEntityToUpdateDto(updatedProduct);
    }

    @Transactional
    public UpdateProductDto updateProductJson(@Valid UpdateProductDto updateProductDto) {
        Optional<ProductEntity> existingProductOpt = productRepo.findById(updateProductDto.getId());
        if(existingProductOpt.isEmpty()){
            throw new RuntimeException("Product not found with id: " + updateProductDto.getId());
        }
        
        ProductEntity existingProduct = existingProductOpt.get();
        
        // Update fields (excluding images in JSON endpoint)
        existingProduct.setTitle(updateProductDto.getTitle());
        existingProduct.setDescription(updateProductDto.getDescription());
        existingProduct.setPrice(updateProductDto.getPrice());
        existingProduct.setOriginalPrice(updateProductDto.getOriginalPrice());
        existingProduct.setConditionType(updateProductDto.getConditionType());
        existingProduct.setBrand(updateProductDto.getBrand());
        existingProduct.setSize(updateProductDto.getSize());
        existingProduct.setColor(updateProductDto.getColor());
        existingProduct.setMaterial(updateProductDto.getMaterial());
        existingProduct.setCategoryId(updateProductDto.getCategoryId());
        
        if (updateProductDto.getStatus() != null) {
            existingProduct.setStatus(updateProductDto.getStatus());
        }
        if (updateProductDto.getLocation() != null) {
            existingProduct.setLocation(updateProductDto.getLocation());
        }
        if (updateProductDto.getShippingCost() != null) {
            existingProduct.setShippingCost(updateProductDto.getShippingCost());
        }
        if (updateProductDto.getIsNegotiable() != null) {
            existingProduct.setIsNegotiable(updateProductDto.getIsNegotiable());
        }
        if (updateProductDto.getIsFeatured() != null) {
            existingProduct.setIsFeatured(updateProductDto.getIsFeatured());
        }
        
        ProductEntity savedProduct = productRepo.save(existingProduct);
        return mapEntityToUpdateDto(savedProduct);
    }

    public ProductDto getProductById(Long id) {
        ProductEntity product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        return mapEntityToDto(product);
    }

    public boolean isProductExist(Long id) {
        return productRepo.existsById(id);
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        // Delete all images first
        productImageRepo.deleteByProductId(id);
        
        // Then delete the product
        productRepo.deleteById(id);
        return true;
    }

    public PaginationResponse<ProductDto> getPaginatedProducts(Pageable pageable, String keyword) {
        Page<ProductEntity> page;

        if (keyword == null || keyword.trim().isEmpty()) {
            page = productRepo.findAll(pageable);
        } else {
            page = productRepo.findByTitleContainingIgnoreCase(keyword, pageable);
        }

        Page<ProductDto> dtoPage = page.map(this::mapEntityToDto);
        return PaginationUtil.buildPageResponse(dtoPage);
    }

    public List<ProductDto> getByCategoryId(Long id) {
        List<ProductEntity> products = productRepo.findByCategoryId(id);
        return products.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public List<ProductDto> getBySellerId(Long id) {
        List<ProductEntity> products = productRepo.findBySellerId(id);
        return products.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public List<ProductDto> getFeaturedProducts() {
        List<ProductEntity> products = productRepo.findByIsFeaturedTrueAndStatusOrderByCreatedAtDesc(ProductEntity.Status.active);
        return products.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public List<ProductDto> searchProducts(String query, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String conditionType) {
        List<ProductEntity> products = productRepo.searchProducts(query, categoryId, minPrice, maxPrice, conditionType);
        return products.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public String getCountProducts() {
        String count = String.valueOf(productRepo.count());
        return count;
    }
}
