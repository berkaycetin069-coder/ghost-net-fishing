package de.iu.ghostnet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class GhostNet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Breitengrad ist erforderlich.")
    @DecimalMin(value = "-90.0", message = "Der Breitengrad muss mindestens -90 betragen.")
    @DecimalMax(value = "90.0", message = "Der Breitengrad darf höchstens 90 betragen.")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "Längengrad ist erforderlich.")
    @DecimalMin(value = "-180.0", message = "Der Längengrad muss mindestens -180 betragen.")
    @DecimalMax(value = "180.0", message = "Der Längengrad darf höchstens 180 betragen.")
    @Column(nullable = false)
    private Double longitude;

    @NotNull(message = "Die geschätzte Größe ist erforderlich.")
    @Positive(message = "Die geschätzte Größe muss größer als 0 sein.")
    @Column(nullable = false)
    private Double estimatedSize;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GhostNetStatus status = GhostNetStatus.REPORTED;

    // ManyToOne bildet ab: Viele Geisternetze können von derselben Person übernommen werden.
    @ManyToOne
    @JoinColumn(name = "assigned_recovery_person_id")
    private RecoveryPerson assignedRecoveryPerson;

    // Meldedaten für den Status MISSING. Diese Person ist fachlich keine bergende Person.
    private String missingReporterName;

    private String missingReporterPhone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getEstimatedSize() {
        return estimatedSize;
    }

    public void setEstimatedSize(Double estimatedSize) {
        this.estimatedSize = estimatedSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GhostNetStatus getStatus() {
        return status;
    }

    public void setStatus(GhostNetStatus status) {
        this.status = status;
    }

    public RecoveryPerson getAssignedRecoveryPerson() {
        return assignedRecoveryPerson;
    }

    public void setAssignedRecoveryPerson(RecoveryPerson assignedRecoveryPerson) {
        this.assignedRecoveryPerson = assignedRecoveryPerson;
    }

    public String getMissingReporterName() {
        return missingReporterName;
    }

    public void setMissingReporterName(String missingReporterName) {
        this.missingReporterName = missingReporterName;
    }

    public String getMissingReporterPhone() {
        return missingReporterPhone;
    }

    public void setMissingReporterPhone(String missingReporterPhone) {
        this.missingReporterPhone = missingReporterPhone;
    }

    public boolean isOpenForRecovery() {
        return status == GhostNetStatus.REPORTED && assignedRecoveryPerson == null;
    }

    public boolean isRecoverable() {
        return status == GhostNetStatus.RECOVERY_PENDING && assignedRecoveryPerson != null;
    }

    public boolean isMissingReportAllowed() {
        return status == GhostNetStatus.REPORTED || status == GhostNetStatus.RECOVERY_PENDING;
    }

    public boolean isRecovered() {
        return status == GhostNetStatus.RECOVERED;
    }

    public boolean isMissing() {
        return status == GhostNetStatus.MISSING;
    }

    public boolean isTerminalStatus() {
        return status == GhostNetStatus.RECOVERED || status == GhostNetStatus.MISSING;
    }
}
