package ru.yandex.practicum.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.service.ShoppingStoreService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreClient {
    private final ShoppingStoreService shoppingStoreService;

    @GetMapping
    @Override
    public List<ProductDto> getProducts(
            @RequestParam @NotNull ProductCategory category, 
            Pageable pageable) {
        return shoppingStoreService.getProducts(category, pageable);
    }

    @GetMapping("/{productId}")
    @Override
    public ProductDto getProduct(@PathVariable UUID productId) {
        return shoppingStoreService.getProductById(productId);
    }

    @PostMapping
    @Override
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return shoppingStoreService.createProduct(productDto);
    }

    @PutMapping
    @Override
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    @DeleteMapping("/{productId}")
    @Override
    public void removeProduct(@PathVariable UUID productId) {
        shoppingStoreService.removeProduct(productId);
    }

    @PatchMapping("/quantity")
    @Override
    public void updateQuantityState(
            @RequestBody SetProductQuantityStateRequest request) {
        shoppingStoreService.updateQuantityState(request);
    }
}