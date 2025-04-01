package ru.yandex.practicum.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    UUID id;
    
    @NotBlank(message = "Product name cannot be blank")
    String name;
    
    @NotBlank(message = "Description cannot be blank")
    String description;
    
    String imageUrl;
    
    @NotNull(message = "Quantity state must be specified")
    QuantityState quantityState;
    
    @NotNull(message = "Product state must be specified")
    ProductState status;
    
    @DecimalMin(value = "1.0", message = "Rating must be at least 1")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5")
    Double rating;
    
    @NotNull(message = "Category must be specified")
    ProductCategory category;
    
    @NotNull(message = "Price must be specified")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    Double price;
}