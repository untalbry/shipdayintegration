package com.emeth.shipday.application.service;

import com.emeth.shipday.application.port.in.GetCarriersUseCase;
import com.emeth.shipday.application.port.out.CarrierProviderPort;
import com.emeth.shipday.domain.exception.MissingApiKeyException;
import com.emeth.shipday.domain.model.Carrier;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GetCarriersService implements GetCarriersUseCase {

    private final CarrierProviderPort carrierProvider;

    public GetCarriersService(CarrierProviderPort carrierProvider) {
        this.carrierProvider = carrierProvider;
    }

    @Override
    public List<Carrier> getAllCarriers(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new MissingApiKeyException("A Shipday API key is required");
        }
        return carrierProvider.findAll(apiKey.strip());
    }
}
