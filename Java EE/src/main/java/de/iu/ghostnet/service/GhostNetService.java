package de.iu.ghostnet.service;

import de.iu.ghostnet.entity.GhostNet;
import de.iu.ghostnet.entity.GhostNetStatus;
import de.iu.ghostnet.entity.RecoveryPerson;
import de.iu.ghostnet.exception.GhostNetNotFoundException;
import de.iu.ghostnet.exception.InvalidGhostNetStatusException;
import de.iu.ghostnet.repository.GhostNetRepository;
import de.iu.ghostnet.repository.RecoveryPersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GhostNetService {

    private final GhostNetRepository ghostNetRepository;
    private final RecoveryPersonRepository recoveryPersonRepository;

    public GhostNetService(GhostNetRepository ghostNetRepository,
                           RecoveryPersonRepository recoveryPersonRepository) {
        this.ghostNetRepository = ghostNetRepository;
        this.recoveryPersonRepository = recoveryPersonRepository;
    }

    public GhostNet reportGhostNet(GhostNet ghostNet) {
        // Meldungen dürfen anonym erfolgen: Es wird bewusst keine RecoveryPerson gesetzt.
        ghostNet.setId(null);
        ghostNet.setStatus(GhostNetStatus.REPORTED);
        ghostNet.setAssignedRecoveryPerson(null);
        ghostNet.setMissingReporterName(null);
        ghostNet.setMissingReporterPhone(null);
        return ghostNetRepository.save(ghostNet);
    }

    @Transactional(readOnly = true)
    public List<GhostNet> findOpenGhostNets() {
        return ghostNetRepository.findByStatusInOrderByIdDesc(
                List.of(GhostNetStatus.REPORTED, GhostNetStatus.RECOVERY_PENDING)
        );
    }

    @Transactional(readOnly = true)
    public long countOpenGhostNets() {
        return ghostNetRepository.countByStatusIn(
                List.of(GhostNetStatus.REPORTED, GhostNetStatus.RECOVERY_PENDING)
        );
    }

    @Transactional(readOnly = true)
    public GhostNet findById(Long id) {
        return ghostNetRepository.findById(id)
                .orElseThrow(() -> new GhostNetNotFoundException(id));
    }

    public GhostNet assignRecoveryPerson(Long ghostNetId, RecoveryPerson formPerson) {
        GhostNet ghostNet = findById(ghostNetId);

        // Fachregel: Ein Geisternetz darf maximal einer bergenden Person zugeordnet werden.
        if (ghostNet.isTerminalStatus()) {
            throw new InvalidGhostNetStatusException("Dieses Netz wurde bereits abgeschlossen.");
        }
        if (ghostNet.getStatus() != GhostNetStatus.REPORTED) {
            throw new InvalidGhostNetStatusException("Nur gemeldete Netze können übernommen werden.");
        }
        if (ghostNet.getAssignedRecoveryPerson() != null) {
            throw new InvalidGhostNetStatusException("Dieses Netz wurde bereits einer bergenden Person zugeordnet.");
        }

        validateRecoveryPerson(formPerson);

        RecoveryPerson recoveryPerson = recoveryPersonRepository.findByNameIgnoreCaseAndPhone(
                        formPerson.getName(), formPerson.getPhone())
                .orElseGet(() -> recoveryPersonRepository.save(formPerson));

        ghostNet.setAssignedRecoveryPerson(recoveryPerson);
        ghostNet.setStatus(GhostNetStatus.RECOVERY_PENDING);
        return ghostNetRepository.save(ghostNet);
    }

    public GhostNet markRecovered(Long ghostNetId) {
        GhostNet ghostNet = findById(ghostNetId);

        if (ghostNet.isTerminalStatus()) {
            throw new InvalidGhostNetStatusException("Dieses Netz wurde bereits abgeschlossen.");
        }
        if (!ghostNet.isRecoverable()) {
            throw new InvalidGhostNetStatusException("Nur übernommene Netze können als geborgen markiert werden.");
        }

        ghostNet.setStatus(GhostNetStatus.RECOVERED);
        return ghostNetRepository.save(ghostNet);
    }

    public GhostNet markMissing(Long ghostNetId, String reporterName, String reporterPhone) {
        GhostNet ghostNet = findById(ghostNetId);

        if (!ghostNet.isMissingReportAllowed()) {
            throw new InvalidGhostNetStatusException("Dieses Netz wurde bereits abgeschlossen.");
        }

        validateMissingReporter(reporterName, reporterPhone);

        // Wichtig: Die meldende Person ist nicht automatisch die bergende Person.
        ghostNet.setMissingReporterName(reporterName.trim());
        ghostNet.setMissingReporterPhone(reporterPhone.trim());
        ghostNet.setStatus(GhostNetStatus.MISSING);
        return ghostNetRepository.save(ghostNet);
    }

    private void validateMissingReporter(String reporterName, String reporterPhone) {
        if (reporterName == null || reporterName.isBlank()) {
            throw new IllegalArgumentException("Name und Telefonnummer sind erforderlich.");
        }
        if (reporterPhone == null || reporterPhone.isBlank()) {
            throw new IllegalArgumentException("Name und Telefonnummer sind erforderlich.");
        }
    }

    private void validateRecoveryPerson(RecoveryPerson recoveryPerson) {
        if (recoveryPerson == null
                || recoveryPerson.getName() == null
                || recoveryPerson.getName().isBlank()
                || recoveryPerson.getPhone() == null
                || recoveryPerson.getPhone().isBlank()) {
            throw new IllegalArgumentException("Name und Telefonnummer sind erforderlich.");
        }
    }
}
