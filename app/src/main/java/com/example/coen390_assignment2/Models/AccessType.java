package com.example.coen390_assignment2.Models;

public enum AccessType {
    CREATED("Created"),
    OPENED("Opened"),
    CLOSED("Closed"),
    DELETED("Deleted");

    private String accessType;

    AccessType(String accessType) {
        this.accessType = accessType;
    }

    public static AccessType matchEnum(String input) {
        for (AccessType enumValue : AccessType.values()) {
            if (enumValue.getStringAccessType().equals(input)) {
                return enumValue;
            }
        }

        return null;
    }

    public String getStringAccessType() {
        return this.accessType;
    }
}
