package ru.yandex.practicum.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pageable {
    @Min(value = 0, message = "Page number must not be negative")
    private int page;

    @Positive(message = "Page size must be positive")
    private int size;

    @NotNull
    private List<String> sort = Collections.emptyList();
}