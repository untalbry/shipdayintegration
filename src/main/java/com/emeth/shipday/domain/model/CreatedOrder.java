package com.emeth.shipday.domain.model;

/**
 * Result of creating an order: the id Shipday assigned to it.
 */
public record CreatedOrder(Long orderId, String orderNumber) {
}
