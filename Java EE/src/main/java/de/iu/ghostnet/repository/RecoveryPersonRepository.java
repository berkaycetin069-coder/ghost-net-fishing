package de.iu.ghostnet.repository;

import de.iu.ghostnet.entity.RecoveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecoveryPersonRepository extends JpaRepository<RecoveryPerson, Long> {

    Optional<RecoveryPerson> findByNameIgnoreCaseAndPhone(String name, String phone);
}
