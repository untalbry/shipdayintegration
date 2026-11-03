package com.emeth.shipday.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Data required to create a delivery order in Shipday.
 */
public record NewOrder(
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
        List<OrderItem> items) {
}
