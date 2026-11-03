package com.emeth.shipday.infrastructure.adapter.in.rest.dto;

import com.emeth.shipday.domain.model.CreatedOrder;

public record CreateOrderResponse(Long orderId, String orderNumber) {

    public static CreateOrderResponse from(CreatedOrder order) {
        return new CreateOrderResponse(order.orderId(), order.orderNumber());
    }
}
