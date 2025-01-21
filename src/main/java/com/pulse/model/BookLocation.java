package com.pulse.model;

public enum BookLocation {
    SHELF_A("Shelf A - Fiction"),
    SHELF_B("Shelf B - Non-Fiction"),
    SHELF_C("Shelf C - Reference"),
    SHELF_D("Shelf D - Academic"),
    SHELF_E("Shelf E - Children's Books"),
    SHELF_F("Shelf F - Magazines"),
    ARCHIVE("Archive Room");

    private final String displayName;

    BookLocation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 