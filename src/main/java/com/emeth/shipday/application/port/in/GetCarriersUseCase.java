package com.emeth.shipday.application.port.in;

import com.emeth.shipday.domain.model.Carrier;

import java.util.List;

/**
 * Inbound port: retrieve every carrier registered in the account that owns the API key.
 */
public interface GetCarriersUseCase {

    List<Carrier> getAllCarriers(String apiKey);
}
