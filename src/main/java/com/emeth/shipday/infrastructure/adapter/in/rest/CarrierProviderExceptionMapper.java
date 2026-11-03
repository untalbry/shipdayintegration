package com.emeth.shipday.infrastructure.adapter.in.rest;

import com.emeth.shipday.domain.exception.CarrierProviderException;
import com.emeth.shipday.domain.exception.MissingApiKeyException;
import com.emeth.shipday.infrastructure.adapter.in.rest.dto.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class CarrierProviderExceptionMapper {

    private static final Logger LOG = Logger.getLogger(CarrierProviderExceptionMapper.class);

    @ServerExceptionMapper
    public Response map(CarrierProviderException e) {
        LOG.error("Shipday request failed", e);
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
    public Response map(MissingApiKeyException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse("MISSING_API_KEY", e.getMessage()))
                .build();
    }
}
