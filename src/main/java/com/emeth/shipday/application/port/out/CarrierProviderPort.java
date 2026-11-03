package com.emeth.shipday.application.port.out;

import com.emeth.shipday.domain.model.Carrier;

import java.util.List;

/**
 * Outbound port: source of carriers (implemented by the Shipday adapter).
 */
public interface CarrierProviderPort {

    List<Carrier> findAll(String apiKey);
}
