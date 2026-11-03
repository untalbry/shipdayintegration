package com.emeth.shipday.infrastructure.adapter.in.rest.dto;

import com.emeth.shipday.domain.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemRequest(
        String name,
        BigDecimal unitPrice,
        Integer quantity,
        List<String> addOns,
        String detail) {

    OrderItem toDomain() {
        return new OrderItem(
                name,
                unitPrice,
                quantity == null ? 0 : quantity,
                addOns == null ? List.of() : List.copyOf(addOns),
                detail);
    }
}
