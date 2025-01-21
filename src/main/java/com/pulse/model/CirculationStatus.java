package com.pulse.model;

public enum CirculationStatus {
    BORROWED("Borrowed"),
    RETURNED("Returned"),
    OVERDUE("Overdue"),
    RENEWED("Renewed");

    private final String displayName;

    CirculationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 