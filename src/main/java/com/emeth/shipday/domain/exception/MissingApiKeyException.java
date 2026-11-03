package com.emeth.shipday.domain.exception;

/**
 * Raised when a request arrives without the Shipday API key.
 */
public class MissingApiKeyException extends RuntimeException {

    public MissingApiKeyException(String message) {
        super(message);
    }
}
