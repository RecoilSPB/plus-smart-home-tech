package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.delivery.dto.DeliveryRequest;
import ru.yandex.practicum.delivery.enums.DeliveryStatus;
import ru.yandex.practicum.exception.DeliveryNotFoundException;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.order.client.OrderClient;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.utils.DeliveryUtil;
import ru.yandex.practicum.warehouse.client.WarehouseClient;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private static final double WEIGHT_RATE = 0.3;
    private static final double VOLUME_RATE = 0.2;

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final AddressMapper addressMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        log.info("Creating delivery for order id = {}", deliveryDto.getOrderId());
        Delivery delivery = buildDeliveryEntity(deliveryDto);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(savedDelivery);
    }

    @Override
    @Transactional
    public void completeDelivery(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.deliverOrder(orderId);
    }

    @Override
    @Transactional
    public void confirmPickup(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryStatus(DeliveryStatus.IN_PROGRESS);
        deliveryRepository.save(delivery);

        DeliveryRequest request = getNewShippedToDeliveryRequest(delivery);
        warehouseClient.shippedToDelivery(request);
    }

    @Override
    @Transactional
    public void failDelivery(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryStatus(DeliveryStatus.FAILED);
        deliveryRepository.save(delivery);
        orderClient.failDeliverOrder(orderId);
    }

    @Override
    public Double calculateDeliveryCost(OrderDto orderDto) {
        Delivery delivery = getDeliveryByOrderId(orderDto.getOrderId());
        log.info("delivery for calc cost: {}", delivery);
        double cost = DeliveryUtil.BASE_DELIVERY_PRICE;
        cost += DeliveryUtil.BASE_DELIVERY_PRICE * getCoefByFromAddress(delivery.getSenderAddress());
        cost *= getFragileCoefficient(orderDto.isFragile());
        cost += orderDto.getDeliveryWeight() * WEIGHT_RATE;
        cost += orderDto.getDeliveryVolume() * VOLUME_RATE;
        cost *= getCoefByToAddress(delivery.getSenderAddress(), delivery.getRecipientAddress());
        return cost;
    }

    double getCoefByFromAddress(Address address) {
        String addressStr = address.toString();
        if (addressStr.contains("ADDRESS_1")) {
            return DeliveryUtil.ADDRESS_1_ADDRESS_COEF;
        } else if (addressStr.contains("ADDRESS_2")) {
            return DeliveryUtil.ADDRESS_2_ADDRESS_COEF;
        } else {
            return DeliveryUtil.BASE_ADDRESS_COEF;
        }
    }

    double getCoefByToAddress(Address from, Address to) {
        if (!from.getStreet().equals(to.getStreet())) {
            return DeliveryUtil.DIFF_STREET_ADDRESS_COEF;
        }

        return 1.0;
    }

    double getFragileCoefficient(boolean isFragile) {
        return isFragile ? DeliveryUtil.FRAGILE_COEF : 1.0;
    }

    private Delivery getDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId).orElseThrow(
                () -> new DeliveryNotFoundException("Не найдена доставка")
        );
    }

    private DeliveryRequest getNewShippedToDeliveryRequest(Delivery delivery) {
        return new DeliveryRequest(
                delivery.getOrderId(),
                delivery.getDeliveryId()
        );
    }

    private Delivery buildDeliveryEntity(DeliveryDto dto) {
        return Delivery.builder()
                .orderId(dto.getOrderId())
                .senderAddress(addressMapper.toEntity(dto.getSenderAddress()))
                .recipientAddress(addressMapper.toEntity(dto.getRecipientAddress()))
                .deliveryStatus(DeliveryStatus.CREATED)
                .build();
    }
}
