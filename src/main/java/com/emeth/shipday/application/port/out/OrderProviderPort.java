package com.emeth.shipday.application.port.out;

import com.emeth.shipday.domain.model.CreatedOrder;
import com.emeth.shipday.domain.model.NewOrder;

/**
 * Outbound port: destination of new orders (implemented by the Shipday adapter).
 */
public interface OrderProviderPort {

    CreatedOrder create(String apiKey, NewOrder order);
}
