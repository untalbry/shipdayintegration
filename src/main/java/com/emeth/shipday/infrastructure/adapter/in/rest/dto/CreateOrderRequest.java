package com.emeth.shipday.infrastructure.adapter.in.rest.dto;

import com.emeth.shipday.domain.model.NewOrder;
import com.emeth.shipday.domain.model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Body of POST /api/orders. Field names mirror Shipday's insert-order API.
 * Dates use "yyyy-MM-dd" and times "HH:mm[:ss]".
 */
public record CreateOrderRequest(
        String orderNumber,
        String customerName,
        String customerAddress,
        String customerEmail,
        String customerPhoneNumber,
        String restaurantName,
        String restaurantAddress,
        String restaurantPhoneNumber,
        LocalDate expectedDeliveryDate,
        LocalTime expectedPickupTime,
        LocalTime expectedDeliveryTime,
        Double pickupLatitude,
        Double pickupLongitude,
        Double deliveryLatitude,
        Double deliveryLongitude,
        BigDecimal tips,
        BigDecimal tax,
        BigDecimal discountAmount,
        BigDecimal deliveryFee,
        BigDecimal totalOrderCost,
        String deliveryInstruction,
        String orderSource,
        String additionalId,
        String paymentMethod,
        List<OrderItemRequest> orderItem) {

    public NewOrder toDomain() {
        List<OrderItem> items = orderItem == null ? List.of()
                : orderItem.stream().map(i -> i == null ? null : i.toDomain()).toList();
        return new NewOrder(
                orderNumber,
                customerName,
                customerAddress,
                customerEmail,
                customerPhoneNumber,
                restaurantName,
                restaurantAddress,
                restaurantPhoneNumber,
                expectedDeliveryDate,
                expectedPickupTime,
                expectedDeliveryTime,
                pickupLatitude,
                pickupLongitude,
                deliveryLatitude,
                deliveryLongitude,
                tips,
                tax,
                discountAmount,
                deliveryFee,
                totalOrderCost,
                deliveryInstruction,
                orderSource,
                additionalId,
                paymentMethod,
                items);
    }
}
