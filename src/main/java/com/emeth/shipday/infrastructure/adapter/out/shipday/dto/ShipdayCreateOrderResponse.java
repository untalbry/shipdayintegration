package com.emeth.shipday.infrastructure.adapter.out.shipday.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response of POST https://api.shipday.com/orders, e.g.
 * {"success": true, "response": "Updated Successfully", "orderId": 7543}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipdayCreateOrderResponse(Boolean success, String response, Long orderId) {
}
