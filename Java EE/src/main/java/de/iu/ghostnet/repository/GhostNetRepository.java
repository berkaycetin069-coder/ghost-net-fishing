package de.iu.ghostnet.repository;

import de.iu.ghostnet.entity.GhostNet;
import de.iu.ghostnet.entity.GhostNetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GhostNetRepository extends JpaRepository<GhostNet, Long> {

    List<GhostNet> findByStatusIn(List<GhostNetStatus> statuses);

    List<GhostNet> findByStatusInOrderByIdDesc(List<GhostNetStatus> statuses);

    long countByStatusIn(List<GhostNetStatus> statuses);
}
