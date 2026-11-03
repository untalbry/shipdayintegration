package com.emeth.shipday.infrastructure.adapter.out.shipday.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

/**
 * Payload for POST https://api.shipday.com/orders.
 * Dates are "yyyy-MM-dd" and times "HH:mm:ss", as Shipday expects.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ShipdayCreateOrderRequest(
        String orderNumber,
        String customerName,
        String customerAddress,
        String customerEmail,
        String customerPhoneNumber,
        String restaurantName,
        String restaurantAddress,
        String restaurantPhoneNumber,
        String expectedDeliveryDate,
        String expectedPickupTime,
        String expectedDeliveryTime,
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
        List<ShipdayOrderItemDto> orderItem) {
}
