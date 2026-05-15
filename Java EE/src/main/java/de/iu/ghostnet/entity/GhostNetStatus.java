package de.iu.ghostnet.entity;

public enum GhostNetStatus {
    REPORTED("Gemeldet", "text-bg-primary"),
    RECOVERY_PENDING("Bergung übernommen", "text-bg-warning"),
    RECOVERED("Geborgen", "text-bg-success"),
    MISSING("Verschollen", "text-bg-danger");

    private final String displayName;
    private final String badgeClass;

    GhostNetStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
