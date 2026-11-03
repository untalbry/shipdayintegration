package com.emeth.shipday.domain.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * A line of an order (product, quantity and unit price).
 */
public record OrderItem(
        String name,
        BigDecimal unitPrice,
        int quantity,
        List<String> addOns,
        String detail) {
}
