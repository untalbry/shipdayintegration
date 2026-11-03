package com.emeth.shipday.domain.exception;

/**
 * Raised when the external order provider cannot create the order.
 */
public class OrderProviderException extends RuntimeException {

    private final int upstreamStatus;

    public OrderProviderException(String message, int upstreamStatus, Throwable cause) {
        super(message, cause);
        this.upstreamStatus = upstreamStatus;
    }

    public int getUpstreamStatus() {
        return upstreamStatus;
    }
}
