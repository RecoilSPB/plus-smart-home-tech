package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        Sort sort = Sort.by(pageable.getSort().getFirst());
        PageRequest page = PageRequest.of(pageable.getPage(), pageable.getSize(), sort);

        return productRepository.findAllByProductCategory(category, page)
                .stream()
                .map(productMapper::map)
                .toList();
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        return productMapper.map(getProduct(productId));
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.map(productDto);

        return productMapper.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = getProduct(productDto.getId());
        productMapper.update(productDto, product);
        return productMapper.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public boolean updateQuantityState(SetProductQuantityStateRequest stateRequest) {
        Product product = getProduct(stateRequest.getProductId());
        product.setQuantityState(stateRequest.getQuantityState());
        productRepository.save(product);

        return true;
    }

    @Override
    @Transactional
    public boolean removeProduct(UUID productId) {
        Product product = getProduct(productId);
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);

        return true;
    }

    private Product getProduct(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundException(
                String.format("Ошибка, товар по идентификатору %s в БД не найден", productId))
        );
    }
}
