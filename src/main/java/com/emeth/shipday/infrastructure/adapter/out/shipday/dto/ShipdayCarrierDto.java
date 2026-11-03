package com.emeth.shipday.infrastructure.adapter.out.shipday.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Raw carrier payload returned by GET https://api.shipday.com/carriers.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipdayCarrierDto(
        Long id,
        Long personalId,
        String name,
        String codeName,
        String phoneNumber,
        Long companyId,
        Long areaId,
        Boolean isOnShift,
        String email,
        String carrierPhoto,
        Boolean isActive) {
}
