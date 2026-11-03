package com.emeth.shipday.infrastructure.adapter.out.shipday;

import com.emeth.shipday.application.port.out.OrderProviderPort;
import com.emeth.shipday.domain.exception.OrderProviderException;
import com.emeth.shipday.domain.model.CreatedOrder;
import com.emeth.shipday.domain.model.NewOrder;
import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayCreateOrderResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * Outbound adapter that creates orders in Shipday.
 */
@ApplicationScoped
public class ShipdayOrderAdapter implements OrderProviderPort {

    private final ShipdayApiClient client;

    public ShipdayOrderAdapter(@RestClient ShipdayApiClient client) {
        this.client = client;
    }

    @Override
    public CreatedOrder create(String apiKey, NewOrder order) {
        ShipdayCreateOrderResponse response;
        try {
            response = client.createOrder("Basic " + apiKey, ShipdayOrderMapper.toRequest(order));
        } catch (WebApplicationException e) {
            int status = e.getResponse().getStatus();
            throw new OrderProviderException("Shipday responded with HTTP " + status, status, e);
        } catch (ProcessingException e) {
            throw new OrderProviderException("Could not reach Shipday: " + e.getMessage(), 0, e);
        }
        if (response == null || !Boolean.TRUE.equals(response.success())) {
            String detail = response == null || response.response() == null ? "no details" : response.response();
            throw new OrderProviderException("Shipday did not create the order: " + detail, 200, null);
        }
        return new CreatedOrder(response.orderId(), order.orderNumber());
    }
}
