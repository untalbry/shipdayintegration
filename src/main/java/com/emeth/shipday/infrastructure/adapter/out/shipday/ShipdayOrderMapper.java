package com.emeth.shipday.infrastructure.adapter.out.shipday;

import com.emeth.shipday.domain.model.NewOrder;
import com.emeth.shipday.domain.model.OrderItem;
import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayCreateOrderRequest;
import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayOrderItemDto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

final class ShipdayOrderMapper {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    private ShipdayOrderMapper() {
    }

    static ShipdayCreateOrderRequest toRequest(NewOrder order) {
        return new ShipdayCreateOrderRequest(
                order.orderNumber(),
                order.customerName(),
                order.customerAddress(),
                order.customerEmail(),
                order.customerPhoneNumber(),
                order.restaurantName(),
                order.restaurantAddress(),
                order.restaurantPhoneNumber(),
                order.expectedDeliveryDate() == null ? null : order.expectedDeliveryDate().toString(),
                time(order.expectedPickupTime()),
                time(order.expectedDeliveryTime()),
                order.pickupLatitude(),
                order.pickupLongitude(),
                order.deliveryLatitude(),
                order.deliveryLongitude(),
                order.tips(),
                order.tax(),
                order.discountAmount(),
                order.deliveryFee(),
                order.totalOrderCost(),
                order.deliveryInstruction(),
                order.orderSource(),
                order.additionalId(),
                order.paymentMethod(),
                order.items() == null || order.items().isEmpty() ? null
                        : order.items().stream().map(ShipdayOrderMapper::toItem).toList());
    }

    private static ShipdayOrderItemDto toItem(OrderItem item) {
        List<String> addOns = item.addOns() == null || item.addOns().isEmpty() ? null : item.addOns();
        return new ShipdayOrderItemDto(item.name(), item.unitPrice(), item.quantity(), addOns, item.detail());
    }

    private static String time(LocalTime time) {
        return time == null ? null : time.format(TIME);
    }
}
