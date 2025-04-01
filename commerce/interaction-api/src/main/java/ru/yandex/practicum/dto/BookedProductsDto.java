package ru.yandex.practicum.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class BookedProductsDto {
    @PositiveOrZero(message = "Delivery weight must be positive or zero")
    Double deliveryWeight;
    
    @PositiveOrZero(message = "Delivery volume must be positive or zero")
    Double deliveryVolume;
    
    Boolean fragile;
}