package com.emeth.shipday.infrastructure.adapter.in.rest.dto;

import com.emeth.shipday.domain.model.Carrier;

public record CarrierResponse(
        Long id,
        Long personalId,
        String name,
        String codeName,
        String phoneNumber,
        String email,
        Long companyId,
        Long areaId,
        boolean onShift,
        boolean active,
        String photoUrl) {

    public static CarrierResponse from(Carrier carrier) {
        return new CarrierResponse(
                carrier.id(),
                carrier.personalId(),
                carrier.name(),
                carrier.codeName(),
                carrier.phoneNumber(),
                carrier.email(),
                carrier.companyId(),
                carrier.areaId(),
                carrier.onShift(),
                carrier.active(),
                carrier.photoUrl());
    }
}
