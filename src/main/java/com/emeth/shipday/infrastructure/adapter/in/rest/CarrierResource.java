package com.emeth.shipday.infrastructure.adapter.in.rest;

import com.emeth.shipday.application.port.in.GetCarriersUseCase;
import com.emeth.shipday.infrastructure.adapter.in.rest.dto.CarrierResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/carriers")
@Produces(MediaType.APPLICATION_JSON)
public class CarrierResource {

    public static final String API_KEY_HEADER = "X-Shipday-Api-Key";

    private final GetCarriersUseCase getCarriersUseCase;

    public CarrierResource(GetCarriersUseCase getCarriersUseCase) {
        this.getCarriersUseCase = getCarriersUseCase;
    }

    @GET
    public List<CarrierResponse> getAll(@HeaderParam(API_KEY_HEADER) String apiKey) {
        return getCarriersUseCase.getAllCarriers(apiKey).stream()
                .map(CarrierResponse::from)
                .toList();
    }
}
