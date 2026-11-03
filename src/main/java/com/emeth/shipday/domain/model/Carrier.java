package com.emeth.shipday.domain.model;

/**
 * Domain representation of a Shipday carrier (driver).
 */
public record Carrier(
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
}
