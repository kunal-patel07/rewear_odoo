package com.example.cafe_backedn.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.dto.ProductDto;
import com.example.cafe_backedn.dto.UpdateProductDto;
import com.example.cafe_backedn.entity.ProductEntity;
import com.example.cafe_backedn.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProduct(){
        List<ProductDto> allProduct = productService.getAllProduct();
        if(allProduct == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allProduct);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> storeProduct(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "originalPrice", required = false) BigDecimal originalPrice,
            @RequestParam("conditionType") String conditionType,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "size", required = false) String size,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "material", required = false) String material,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("sellerId") Long sellerId,
            @RequestParam(value = "status", required = false, defaultValue = "active") String status,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "shippingCost", required = false, defaultValue = "0.00") BigDecimal shippingCost,
            @RequestParam(value = "isNegotiable", required = false, defaultValue = "true") Boolean isNegotiable,
            @RequestParam(value = "isFeatured", required = false, defaultValue = "false") Boolean isFeatured,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles,
            HttpServletRequest request
    ) {
        try {
            // Debug all request parameters
            System.out.println("=== CREATE PRODUCT DEBUG ===");
            System.out.println("Content-Type: " + request.getContentType());
            System.out.println("Method: " + request.getMethod());
            System.out.println("All parameter names: " + java.util.Collections.list(request.getParameterNames()));
            
            // Debug each parameter
            System.out.println("title: " + title);
            System.out.println("description: " + description);
            System.out.println("price: " + price);
            System.out.println("originalPrice: " + originalPrice);
            System.out.println("conditionType: " + conditionType);
            System.out.println("brand: " + brand);
            System.out.println("size: " + size);
            System.out.println("color: " + color);
            System.out.println("material: " + material);
            System.out.println("categoryId: " + categoryId);
            System.out.println("sellerId: " + sellerId);
            System.out.println("status: " + status);
            System.out.println("location: " + location);
            System.out.println("shippingCost: " + shippingCost);
            System.out.println("isNegotiable: " + isNegotiable);
            System.out.println("isFeatured: " + isFeatured);
            
            // Debug image files
            System.out.println("Image files count: " + (imageFiles != null ? imageFiles.size() : 0));
            if (imageFiles != null) {
                for (int i = 0; i < imageFiles.size(); i++) {
                    MultipartFile file = imageFiles.get(i);
                    System.out.println("Image " + i + ": " + (file != null ? file.getOriginalFilename() : "NULL") + 
                                     " (size: " + (file != null ? file.getSize() : "NULL") + ")");
                }
            }

            // Upload images to Cloudinary
            List<String> imageUrls = new ArrayList<>();
            if (imageFiles != null && !imageFiles.isEmpty()) {
                for (MultipartFile imageFile : imageFiles) {
                    if (imageFile != null && !imageFile.isEmpty()) {
                        File uploadedFile = File.createTempFile("temp", imageFile.getOriginalFilename());
                        imageFile.transferTo(uploadedFile);

                        Map<?, ?> result = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
                        String imageUrl = result.get("secure_url").toString();
                        imageUrls.add(imageUrl);
                        
                        System.out.println("Uploaded image: " + imageUrl);
                        
                        // Clean up temp file
                        uploadedFile.delete();
                    }
                }
            }

            // Create DTO manually
            ProductDto data = new ProductDto();
            data.setTitle(title);
            data.setDescription(description);
            data.setPrice(price);
            data.setOriginalPrice(originalPrice);
            data.setConditionType(ProductEntity.ConditionType.valueOf(conditionType));
            data.setBrand(brand);
            data.setSize(size);
            data.setColor(color);
            data.setMaterial(material);
            data.setCategoryId(categoryId);
            data.setSellerId(sellerId);
            data.setStatus(ProductEntity.Status.valueOf(status));
            data.setLocation(location);
            data.setShippingCost(shippingCost);
            data.setIsNegotiable(isNegotiable);
            data.setIsFeatured(isFeatured);

            System.out.println("ProductDto created with " + imageUrls.size() + " images");

            // Save product
            ProductDto saved = productService.createProduct(data, imageUrls);
            
            System.out.println("ProductDto saved with ID: " + saved.getId());
            System.out.println("=== END CREATE PRODUCT DEBUG ===");
            
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            System.err.println("Error creating product: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create-json")
    public ResponseEntity<ProductDto> storeProductJson(@Valid @RequestBody ProductDto productDto) {
        ProductDto saved = productService.createProduct(productDto, new ArrayList<>());
        if(saved == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getOneProduct(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdateProductDto> updateProduct(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "originalPrice", required = false) BigDecimal originalPrice,
            @RequestParam("conditionType") String conditionType,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "size", required = false) String size,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "material", required = false) String material,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "shippingCost", required = false) BigDecimal shippingCost,
            @RequestParam(value = "isNegotiable", required = false) Boolean isNegotiable,
            @RequestParam(value = "isFeatured", required = false) Boolean isFeatured,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles,
            HttpServletRequest request
    ) {
        try {
            // Debug all request parameters
            System.out.println("=== UPDATE PRODUCT DEBUG ===");
            System.out.println("Product ID: " + id);
            System.out.println("Image files count: " + (imageFiles != null ? imageFiles.size() : 0));

            // Upload new images to Cloudinary
            List<String> imageUrls = new ArrayList<>();
            if (imageFiles != null && !imageFiles.isEmpty()) {
                for (MultipartFile imageFile : imageFiles) {
                    if (imageFile != null && !imageFile.isEmpty()) {
                        File uploadedFile = File.createTempFile("temp", imageFile.getOriginalFilename());
                        imageFile.transferTo(uploadedFile);

                        Map<?, ?> result = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
                        String imageUrl = result.get("secure_url").toString();
                        imageUrls.add(imageUrl);
                        
                        System.out.println("Uploaded new image: " + imageUrl);
                        
                        // Clean up temp file
                        uploadedFile.delete();
                    }
                }
            }

            UpdateProductDto data = new UpdateProductDto();
            data.setTitle(title);
            data.setDescription(description);
            data.setPrice(price);
            data.setOriginalPrice(originalPrice);
            data.setConditionType(ProductEntity.ConditionType.valueOf(conditionType));
            data.setBrand(brand);
            data.setSize(size);
            data.setColor(color);
            data.setMaterial(material);
            data.setCategoryId(categoryId);
            if (status != null) {
                data.setStatus(ProductEntity.Status.valueOf(status));
            }
            data.setLocation(location);
            data.setShippingCost(shippingCost);
            data.setIsNegotiable(isNegotiable);
            data.setIsFeatured(isFeatured);

            UpdateProductDto updated = productService.updateProduct(data, id, imageUrls);
            
            System.out.println("Product updated with ID: " + updated.getId());
            System.out.println("=== END UPDATE PRODUCT DEBUG ===");
            
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update-json/{id}")
    public ResponseEntity<UpdateProductDto> updateProductJson(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateProductDto updateProductDto
    ) {
        updateProductDto.setId(id);
        UpdateProductDto updated = productService.updateProductJson(updateProductDto);
        if(updated == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(updated);
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
        response.put("count", productService.getCountProducts());
        return response;
    }

    @GetMapping("/getByCategory/{id}")
    public ResponseEntity<List<ProductDto>> getByCategoryId(@PathVariable Long id){
        List<ProductDto> allProduct = productService.getByCategoryId(id);
        return ResponseEntity.ok(allProduct);
    }

    @GetMapping("/getBySeller/{id}")
    public ResponseEntity<List<ProductDto>> getBySellerId(@PathVariable Long id){
        List<ProductDto> allProduct = productService.getBySellerId(id);
        return ResponseEntity.ok(allProduct);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ProductDto>> getFeaturedProducts(){
        List<ProductDto> featuredProducts = productService.getFeaturedProducts();
        return ResponseEntity.ok(featuredProducts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(
            @RequestParam String query,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String conditionType
    ) {
        List<ProductDto> products = productService.searchProducts(query, categoryId, minPrice, maxPrice, conditionType);
        return ResponseEntity.ok(products);
    }

    // Standalone image upload endpoint
    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadImages(
            @RequestParam(value = "images", required = true) List<MultipartFile> imageFiles,
            HttpServletRequest request
    ) {
        try {
            System.out.println("=== UPLOAD IMAGES DEBUG ===");
            System.out.println("Image files count: " + (imageFiles != null ? imageFiles.size() : 0));
            
            if (imageFiles == null || imageFiles.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "No image files provided");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            List<Map<String, Object>> uploadedImages = new ArrayList<>();
            List<String> imageUrls = new ArrayList<>();

            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile imageFile = imageFiles.get(i);
                
                if (imageFile != null && !imageFile.isEmpty()) {
                    System.out.println("Processing image " + (i + 1) + ": " + imageFile.getOriginalFilename());
                    
                    // Upload to Cloudinary
                    File uploadedFile = File.createTempFile("temp", imageFile.getOriginalFilename());
                    imageFile.transferTo(uploadedFile);

                    Map<?, ?> result = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
                    String imageUrl = result.get("secure_url").toString();
                    String publicId = result.get("public_id").toString();
                    
                    imageUrls.add(imageUrl);
                    
                    // Create detailed response for each image
                    Map<String, Object> imageInfo = new HashMap<>();
                    imageInfo.put("originalName", imageFile.getOriginalFilename());
                    imageInfo.put("url", imageUrl);
                    imageInfo.put("publicId", publicId);
                    imageInfo.put("size", imageFile.getSize());
                    imageInfo.put("contentType", imageFile.getContentType());
                    
                    uploadedImages.add(imageInfo);
                    
                    System.out.println("Uploaded image " + (i + 1) + ": " + imageUrl);
                    
                    // Clean up temp file
                    uploadedFile.delete();
                } else {
                    System.out.println("Skipping empty image file at index " + i);
                }
            }

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Images uploaded successfully");
            response.put("count", uploadedImages.size());
            response.put("images", uploadedImages);
            response.put("urls", imageUrls); // Simple array of URLs for convenience
            
            System.out.println("=== END UPLOAD IMAGES DEBUG ===");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error uploading images: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to upload images: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Single image upload endpoint
    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadSingleImage(
            @RequestParam(value = "image", required = true) MultipartFile imageFile,
            HttpServletRequest request
    ) {
        try {
            System.out.println("=== UPLOAD SINGLE IMAGE DEBUG ===");
            System.out.println("Image file: " + (imageFile != null ? imageFile.getOriginalFilename() : "NULL"));
            
            if (imageFile == null || imageFile.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "No image file provided");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Upload to Cloudinary
            File uploadedFile = File.createTempFile("temp", imageFile.getOriginalFilename());
            imageFile.transferTo(uploadedFile);

            Map<?, ?> result = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            String imageUrl = result.get("secure_url").toString();
            String publicId = result.get("public_id").toString();
            
            System.out.println("Uploaded image: " + imageUrl);
            
            // Clean up temp file
            uploadedFile.delete();

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Image uploaded successfully");
            response.put("originalName", imageFile.getOriginalFilename());
            response.put("url", imageUrl);
            response.put("publicId", publicId);
            response.put("size", imageFile.getSize());
            response.put("contentType", imageFile.getContentType());
            
            System.out.println("=== END UPLOAD SINGLE IMAGE DEBUG ===");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error uploading image: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to upload image: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
