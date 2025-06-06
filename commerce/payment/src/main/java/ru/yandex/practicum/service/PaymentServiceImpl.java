package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentState;
import ru.yandex.practicum.order.client.OrderClient;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.shoppingStore.client.ShoppingStoreClient;
import ru.yandex.practicum.shoppingStore.dto.ProductDto;
import ru.yandex.practicum.utils.PaymentUtil;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    @Transactional
    public PaymentDto createPayment(OrderDto order) {
        Payment savedPayment = paymentRepository.save(getNewPayment(order));
        return paymentMapper.map(savedPayment);
    }

    @Override
    public Double getTotalCost(OrderDto order) {
        return calcTotalCost(order);
    }

    @Override
    @Transactional
    public void paymentSuccess(UUID orderId) {
        updatePaymentState(orderId, PaymentState.SUCCESS);
        orderClient.successPayOrder(orderId);
    }

    @Override
    public Double getProductCost(OrderDto order) {
        return calcProductsCost(order);
    }

    @Override
    @Transactional
    public void paymentFailed(UUID orderId) {
        updatePaymentState(orderId, PaymentState.FAILED);
        orderClient.failPayOrder(orderId);
    }

    private void updatePaymentState(UUID orderId, PaymentState newState) {
        Payment payment = getPaymentByOrderId(orderId);
        payment.setState(newState);
        paymentRepository.save(payment);
    }

    private Payment getPaymentByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new NoPaymentFoundException("Указанная оплата не найдена")
        );
    }

    private Payment getNewPayment(OrderDto order) {
        double fee = calcFeeCost(order.getProductPrice());
        return Payment.builder()
                .orderId(order.getOrderId())
                .state(PaymentState.PENDING)
                .totalPayment(order.getTotalPrice())
                .deliveryTotal(order.getDeliveryPrice())
                .feeTotal(fee)
                .build();
    }

    private double calcTotalCost(OrderDto order) {
        double productCost = calcProductsCost(order);
        double deliveryCost = order.getDeliveryPrice();

        return productCost + calcFeeCost(productCost) + deliveryCost;
    }

    private double calcProductsCost(OrderDto order) {
        double cost = 0;

        Map<UUID, ProductDto> products = shoppingStoreClient.getProductByIds(order.getProducts().keySet())
                .stream()
                .collect(Collectors.toMap(ProductDto::getProductId, Function.identity()));
        for (Map.Entry<UUID, Integer> orderProduct : order.getProducts().entrySet()) {
            int quantity = orderProduct.getValue();
            if (!products.containsKey(orderProduct.getKey())) {
                throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчёта");
            }
            double price = products.get(orderProduct.getKey()).getPrice();
            cost += price * quantity;
        }

        return cost;
    }

    private double calcFeeCost(double cost) {
        return cost * PaymentUtil.BASE_VAT_RATE;
    }
}
