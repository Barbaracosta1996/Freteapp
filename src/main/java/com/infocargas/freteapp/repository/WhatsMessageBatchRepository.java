package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.WhatsMessageBatch;
import com.infocargas.freteapp.domain.enumeration.WhatsStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WhatsMessageBatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WhatsMessageBatchRepository extends JpaRepository<WhatsMessageBatch, Long>, JpaSpecificationExecutor<WhatsMessageBatch> {
    Optional<WhatsMessageBatch> findByWaidToAndStatus(String waid, WhatsStatus status);
}
