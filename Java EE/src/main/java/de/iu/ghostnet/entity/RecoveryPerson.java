package de.iu.ghostnet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class RecoveryPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name ist erforderlich.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Telefonnummer ist erforderlich.")
    @Column(nullable = false)
    private String phone;

    // Rückbeziehung für UML und Nachvollziehbarkeit der 1:n-Beziehung.
    @OneToMany(mappedBy = "assignedRecoveryPerson")
    private List<GhostNet> assignedGhostNets = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<GhostNet> getAssignedGhostNets() {
        return assignedGhostNets;
    }

    public void setAssignedGhostNets(List<GhostNet> assignedGhostNets) {
        this.assignedGhostNets = assignedGhostNets;
    }
}
