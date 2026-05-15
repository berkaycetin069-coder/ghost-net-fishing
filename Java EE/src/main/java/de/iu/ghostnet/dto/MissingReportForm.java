package de.iu.ghostnet.dto;

import jakarta.validation.constraints.NotBlank;

public class MissingReportForm {

    @NotBlank(message = "Name ist erforderlich.")
    private String missingReporterName;

    @NotBlank(message = "Telefonnummer ist erforderlich.")
    private String missingReporterPhone;

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
}
