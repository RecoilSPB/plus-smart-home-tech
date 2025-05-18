package ru.yandex.practicum.mapper;

import org.mapstruct.*;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShoppingCartMapper {

    @Mapping(target = "userName", source = "userName")
    ShoppingCart map(ShoppingCartDto dto, String userName);

    @Mapping(target = "products", source = "items")
    ShoppingCartDto map(ShoppingCart entity);
}
