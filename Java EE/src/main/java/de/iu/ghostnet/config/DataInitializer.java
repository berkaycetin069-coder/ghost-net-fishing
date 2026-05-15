package de.iu.ghostnet.config;

import de.iu.ghostnet.entity.GhostNet;
import de.iu.ghostnet.entity.GhostNetStatus;
import de.iu.ghostnet.entity.RecoveryPerson;
import de.iu.ghostnet.repository.GhostNetRepository;
import de.iu.ghostnet.repository.RecoveryPersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDemoData(GhostNetRepository ghostNetRepository,
                                   RecoveryPersonRepository recoveryPersonRepository) {
        return args -> {
            if (ghostNetRepository.count() > 0) {
                return;
            }

            RecoveryPerson recoveryTeamNorth = new RecoveryPerson();
            recoveryTeamNorth.setName("Bergungsteam Nord");
            recoveryTeamNorth.setPhone("+49 431 123456");

            RecoveryPerson coastalVolunteer = new RecoveryPerson();
            coastalVolunteer.setName("Mara Küste");
            coastalVolunteer.setPhone("+49 471 987654");

            recoveryPersonRepository.save(recoveryTeamNorth);
            recoveryPersonRepository.save(coastalVolunteer);

            GhostNet firstNet = new GhostNet();
            firstNet.setLatitude(54.1789);
            firstNet.setLongitude(7.8891);
            firstNet.setEstimatedSize(25.0);
            firstNet.setDescription("Nahe einer Fahrrinne gemeldetes Netz mit sichtbaren Bojenresten.");

            GhostNet secondNet = new GhostNet();
            secondNet.setLatitude(53.8734);
            secondNet.setLongitude(8.3122);
            secondNet.setEstimatedSize(12.5);
            secondNet.setDescription("Kleineres Netz an einer felsigen Stelle, Sichtung durch Freizeitboot.");

            GhostNet assignedNet = new GhostNet();
            assignedNet.setLatitude(54.5121);
            assignedNet.setLongitude(10.2254);
            assignedNet.setEstimatedSize(40.0);
            assignedNet.setDescription("Größeres Netz nahe einer Muschelbank, Bergung bereits koordiniert.");
            assignedNet.setStatus(GhostNetStatus.RECOVERY_PENDING);
            assignedNet.setAssignedRecoveryPerson(recoveryTeamNorth);

            GhostNet recoveredNet = new GhostNet();
            recoveredNet.setLatitude(53.9912);
            recoveredNet.setLongitude(8.7543);
            recoveredNet.setEstimatedSize(18.0);
            recoveredNet.setDescription("Netz wurde geborgen und im Prototyp als abgeschlossener Fall dokumentiert.");
            recoveredNet.setStatus(GhostNetStatus.RECOVERED);
            recoveredNet.setAssignedRecoveryPerson(coastalVolunteer);

            GhostNet missingNet = new GhostNet();
            missingNet.setLatitude(54.7319);
            missingNet.setLongitude(9.8124);
            missingNet.setEstimatedSize(10.0);
            missingNet.setDescription("Gemeldete Position konnte bei Nachsuche nicht mehr bestätigt werden.");
            missingNet.setStatus(GhostNetStatus.MISSING);
            missingNet.setMissingReporterName("Jan Fischer");
            missingNet.setMissingReporterPhone("+49 4561 112233");

            ghostNetRepository.save(firstNet);
            ghostNetRepository.save(secondNet);
            ghostNetRepository.save(assignedNet);
            ghostNetRepository.save(recoveredNet);
            ghostNetRepository.save(missingNet);
        };
    }
}
