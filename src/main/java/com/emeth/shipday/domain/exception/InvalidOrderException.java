package com.emeth.shipday.domain.exception;

import java.util.List;

/**
 * Raised when an order request is missing required data or contains invalid values.
 */
public class InvalidOrderException extends RuntimeException {

    private final List<String> violations;

    public InvalidOrderException(List<String> violations) {
        super("Invalid order: " + String.join("; ", violations));
        this.violations = List.copyOf(violations);
    }

    public List<String> getViolations() {
        return violations;
    }
}
