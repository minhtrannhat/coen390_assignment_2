package com.example.coen390_assignment2.Models;

import java.time.LocalDateTime;

public class Access {
    private long profileID;

    private AccessType accessType;

    private LocalDateTime timestamp;

    public Access(long profileID, AccessType accessType, LocalDateTime timestamp) {
        this.profileID = profileID;
        this.accessType = accessType;
        this.timestamp = timestamp;
    }

    public long getProfileID() {
        return profileID;
    }

    public void setProfileID(long profileID) {
        this.profileID = profileID;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
