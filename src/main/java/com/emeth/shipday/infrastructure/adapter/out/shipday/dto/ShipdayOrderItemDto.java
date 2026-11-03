package com.emeth.shipday.infrastructure.adapter.out.shipday.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ShipdayOrderItemDto(
        String name,
        BigDecimal unitPrice,
        Integer quantity,
        List<String> addOns,
        String detail) {
}
