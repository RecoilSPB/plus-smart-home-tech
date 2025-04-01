package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/* todo ЗАГЛУШКА Реализовать методы */

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Override
    public ShoppingCartDto getCart(String userName) {
        return null;
    }

    @Override
    public ShoppingCartDto addProducts(String userName, Map<UUID, Long> products) {
        return null;
    }

    @Override
    public void clearCart(String userName) {

    }

    @Override
    public ShoppingCartDto removeProducts(String userName, List<UUID> products) {
        return null;
    }

    @Override
    public ShoppingCartDto updateQuantity(String userName, ChangeProductQuantityRequest request) {
        return null;
    }

    @Override
    public BookedProductsDto bookProducts(String userName) {
        return null;
    }
}