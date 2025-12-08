package com.k41s.ecommerce_api.controllers;

import com.k41s.ecommerce_api.dtos.ProductImageDTO;
import com.k41s.ecommerce_api.entities.ProductImage;
import com.k41s.ecommerce_api.services.ProductImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/productimages")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductImageController {
    private final ProductImageService service;

    // GET: api/productimages/5
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable int id) {
        ProductImage image = service.getImageEntityById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"image-" + id + "\"")
                .body(image.getData());
    }

    // POST: api/productimages/upload/{productId}
    @PostMapping("/upload/{productId}")
    public ResponseEntity<ProductImageDTO> uploadImage(
            @PathVariable int productId,
            @RequestParam("file") MultipartFile file) {

        ProductImageDTO dto = service.uploadImage(productId, file);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // DELETE: api/productimages/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable int id) {
        return service.deleteImage(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
