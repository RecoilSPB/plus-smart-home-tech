package ru.yandex.practicum.delivery.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.order.dto.OrderDto;


import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {

    @PutMapping
    DeliveryDto createDelivery(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void completeDelivery(@RequestBody UUID orderId);

    @PostMapping("/picked")
    void confirmPickup(@RequestBody UUID orderId);

    @PostMapping("/failed")
    void failDelivery(@RequestBody UUID orderId);

    @PostMapping("/cost")
    Double calculateDeliveryCost(@RequestBody OrderDto orderDto);
}
