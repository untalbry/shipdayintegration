package com.emeth.shipday.infrastructure.adapter.out.shipday;

import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayCarrierDto;
import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayCreateOrderRequest;
import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayCreateOrderResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

/**
 * Declarative HTTP client for the Shipday REST API.
 */
@RegisterRestClient(configKey = "shipday-api")
@Produces(MediaType.APPLICATION_JSON)
public interface ShipdayApiClient {

    /**
     * @param authorization value sent as-is, expected format "Basic {API_KEY}"
     */
    @GET
    @Path("/carriers")
    List<ShipdayCarrierDto> getCarriers(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization);

    /**
     * @param authorization value sent as-is, expected format "Basic {API_KEY}"
     */
    @POST
    @Path("/orders")
    @Consumes(MediaType.APPLICATION_JSON)
    ShipdayCreateOrderResponse createOrder(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization,
                                           ShipdayCreateOrderRequest order);
}
