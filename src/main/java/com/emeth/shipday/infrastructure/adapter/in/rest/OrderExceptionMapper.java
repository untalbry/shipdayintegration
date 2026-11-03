package com.emeth.shipday.infrastructure.adapter.in.rest;

import com.emeth.shipday.domain.exception.InvalidOrderException;
import com.emeth.shipday.domain.exception.OrderProviderException;
import com.emeth.shipday.infrastructure.adapter.in.rest.dto.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class OrderExceptionMapper {

    private static final Logger LOG = Logger.getLogger(OrderExceptionMapper.class);

    @ServerExceptionMapper
    public Response map(OrderProviderException e) {
        LOG.error("Shipday order creation failed", e);
        Response.Status status = switch (e.getUpstreamStatus()) {
            case 401, 403 -> Response.Status.UNAUTHORIZED;
            case 0 -> Response.Status.SERVICE_UNAVAILABLE;
            default -> Response.Status.BAD_GATEWAY;
        };
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse("SHIPDAY_ERROR", e.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response map(InvalidOrderException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse("INVALID_ORDER", e.getMessage()))
                .build();
    }
}
