package com.emeth.shipday.application.port.in;

import com.emeth.shipday.domain.model.CreatedOrder;
import com.emeth.shipday.domain.model.NewOrder;

/**
 * Inbound port: create a delivery order in the account that owns the API key.
 */
public interface CreateOrderUseCase {

    CreatedOrder createOrder(String apiKey, NewOrder order);
}
