package com.emeth.shipday.domain.exception;

/**
 * Raised when the external carrier provider cannot fulfil a request.
 */
public class CarrierProviderException extends RuntimeException {

    private final int upstreamStatus;

    public CarrierProviderException(String message, int upstreamStatus, Throwable cause) {
        super(message, cause);
        this.upstreamStatus = upstreamStatus;
    }

    public int getUpstreamStatus() {
        return upstreamStatus;
    }
}
