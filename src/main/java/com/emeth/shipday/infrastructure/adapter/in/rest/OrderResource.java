package com.emeth.shipday.infrastructure.adapter.in.rest;

import com.emeth.shipday.application.port.in.CreateOrderUseCase;
import com.emeth.shipday.domain.model.CreatedOrder;
import com.emeth.shipday.infrastructure.adapter.in.rest.dto.CreateOrderRequest;
import com.emeth.shipday.infrastructure.adapter.in.rest.dto.CreateOrderResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static com.emeth.shipday.infrastructure.adapter.in.rest.CarrierResource.API_KEY_HEADER;

@Path("/api/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final CreateOrderUseCase createOrderUseCase;

    public OrderResource(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @POST
    public Response create(@HeaderParam(API_KEY_HEADER) String apiKey, CreateOrderRequest request) {
        CreatedOrder created = createOrderUseCase.createOrder(apiKey, request == null ? null : request.toDomain());
        return Response.status(Response.Status.CREATED)
                .entity(CreateOrderResponse.from(created))
                .build();
    }
}
