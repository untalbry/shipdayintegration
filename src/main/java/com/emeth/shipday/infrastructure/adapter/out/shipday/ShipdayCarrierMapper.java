package com.emeth.shipday.infrastructure.adapter.out.shipday;

import com.emeth.shipday.domain.model.Carrier;
import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayCarrierDto;

final class ShipdayCarrierMapper {

    private ShipdayCarrierMapper() {
    }

    static Carrier toDomain(ShipdayCarrierDto dto) {
        return new Carrier(
                dto.id(),
                dto.personalId(),
                dto.name(),
                dto.codeName(),
                dto.phoneNumber(),
                dto.email(),
                dto.companyId(),
                dto.areaId(),
                Boolean.TRUE.equals(dto.isOnShift()),
                Boolean.TRUE.equals(dto.isActive()),
                dto.carrierPhoto());
    }
}
