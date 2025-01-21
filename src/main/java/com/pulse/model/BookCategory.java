package com.pulse.model;

public enum BookCategory {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    HISTORY("History"),
    SCIENCE("Science"),
    TECHNOLOGY("Technology"),
    ARTS("Arts"),
    LITERATURE("Literature"),
    REFERENCE("Reference"),
    ACADEMIC("Academic"),
    CHILDREN("Children's Books"),
    MAGAZINE("Magazine"),
    OTHER("Other");

    private final String displayName;

    BookCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 