package de.iu.ghostnet.service;

import de.iu.ghostnet.entity.RecoveryPerson;
import de.iu.ghostnet.repository.RecoveryPersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RecoveryPersonService {

    private final RecoveryPersonRepository recoveryPersonRepository;

    public RecoveryPersonService(RecoveryPersonRepository recoveryPersonRepository) {
        this.recoveryPersonRepository = recoveryPersonRepository;
    }

    public List<RecoveryPerson> findAll() {
        return recoveryPersonRepository.findAll();
    }
}
