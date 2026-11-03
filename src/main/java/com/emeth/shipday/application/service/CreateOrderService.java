package com.emeth.shipday.application.service;

import com.emeth.shipday.application.port.in.CreateOrderUseCase;
import com.emeth.shipday.application.port.out.OrderProviderPort;
import com.emeth.shipday.domain.exception.InvalidOrderException;
import com.emeth.shipday.domain.exception.MissingApiKeyException;
import com.emeth.shipday.domain.model.CreatedOrder;
import com.emeth.shipday.domain.model.NewOrder;
import com.emeth.shipday.domain.model.OrderItem;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class CreateOrderService implements CreateOrderUseCase {

    private static final Set<String> PAYMENT_METHODS = Set.of("cash", "credit_card");

    private final OrderProviderPort orderProvider;

    public CreateOrderService(OrderProviderPort orderProvider) {
        this.orderProvider = orderProvider;
    }

    @Override
    public CreatedOrder createOrder(String apiKey, NewOrder order) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new MissingApiKeyException("A Shipday API key is required");
        }
        validate(order);
        return orderProvider.create(apiKey.strip(), order);
    }

    private static void validate(NewOrder order) {
        if (order == null) {
            throw new InvalidOrderException(List.of("request body is required"));
        }
        List<String> violations = new ArrayList<>();
        requireText(order.orderNumber(), "orderNumber", violations);
        requireText(order.customerName(), "customerName", violations);
        requireText(order.customerAddress(), "customerAddress", violations);
        requireText(order.customerPhoneNumber(), "customerPhoneNumber", violations);
        requireText(order.restaurantName(), "restaurantName", violations);
        requireText(order.restaurantAddress(), "restaurantAddress", violations);

        notNegative(order.tips(), "tips", violations);
        notNegative(order.tax(), "tax", violations);
        notNegative(order.discountAmount(), "discountAmount", violations);
        notNegative(order.deliveryFee(), "deliveryFee", violations);
        notNegative(order.totalOrderCost(), "totalOrderCost", violations);

        latitude(order.pickupLatitude(), "pickupLatitude", violations);
        longitude(order.pickupLongitude(), "pickupLongitude", violations);
        latitude(order.deliveryLatitude(), "deliveryLatitude", violations);
        longitude(order.deliveryLongitude(), "deliveryLongitude", violations);

        if (order.paymentMethod() != null && !PAYMENT_METHODS.contains(order.paymentMethod())) {
            violations.add("paymentMethod must be one of " + PAYMENT_METHODS);
        }

        if (order.items() == null || order.items().isEmpty()) {
            violations.add("orderItem must contain at least one item");
        } else {
            for (int i = 0; i < order.items().size(); i++) {
                OrderItem item = order.items().get(i);
                String prefix = "orderItem[" + i + "].";
                if (item == null) {
                    violations.add(prefix.substring(0, prefix.length() - 1) + " must not be null");
                    continue;
                }
                requireText(item.name(), prefix + "name", violations);
                if (item.quantity() <= 0) {
                    violations.add(prefix + "quantity must be greater than 0");
                }
                if (item.unitPrice() == null) {
                    violations.add(prefix + "unitPrice is required");
                } else {
                    notNegative(item.unitPrice(), prefix + "unitPrice", violations);
                }
            }
        }

        if (!violations.isEmpty()) {
            throw new InvalidOrderException(violations);
        }
    }

    private static void requireText(String value, String field, List<String> violations) {
        if (value == null || value.isBlank()) {
            violations.add(field + " is required");
        }
    }

    private static void notNegative(BigDecimal value, String field, List<String> violations) {
        if (value != null && value.signum() < 0) {
            violations.add(field + " must not be negative");
        }
    }

    private static void latitude(Double value, String field, List<String> violations) {
        if (value != null && (value < -90 || value > 90)) {
            violations.add(field + " must be between -90 and 90");
        }
    }

    private static void longitude(Double value, String field, List<String> violations) {
        if (value != null && (value < -180 || value > 180)) {
            violations.add(field + " must be between -180 and 180");
        }
    }
}
