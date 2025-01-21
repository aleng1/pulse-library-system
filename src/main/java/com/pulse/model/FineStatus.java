package com.pulse.model;

public enum FineStatus {
    PENDING("Pending"),
    PAID("Paid"),
    WAIVED("Waived");

    private final String displayName;

    FineStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 