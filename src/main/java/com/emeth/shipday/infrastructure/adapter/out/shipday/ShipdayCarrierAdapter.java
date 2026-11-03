package com.emeth.shipday.infrastructure.adapter.out.shipday;

import com.emeth.shipday.application.port.out.CarrierProviderPort;
import com.emeth.shipday.domain.exception.CarrierProviderException;
import com.emeth.shipday.domain.model.Carrier;
import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayCarrierDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

/**
 * Outbound adapter that fetches carriers from Shipday.
 */
@ApplicationScoped
public class ShipdayCarrierAdapter implements CarrierProviderPort {

    private final ShipdayApiClient client;

    public ShipdayCarrierAdapter(@RestClient ShipdayApiClient client) {
        this.client = client;
    }

    @Override
    public List<Carrier> findAll(String apiKey) {
        try {
            List<ShipdayCarrierDto> carriers = client.getCarriers("Basic " + apiKey);
            return carriers == null ? List.of() : carriers.stream().map(ShipdayCarrierMapper::toDomain).toList();
        } catch (WebApplicationException e) {
            int status = e.getResponse().getStatus();
            throw new CarrierProviderException("Shipday responded with HTTP " + status, status, e);
        } catch (ProcessingException e) {
            throw new CarrierProviderException("Could not reach Shipday: " + e.getMessage(), 0, e);
        }
    }
}
