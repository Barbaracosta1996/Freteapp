package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.WhatsMessageBatch;
import com.infocargas.freteapp.domain.enumeration.WhatsStatus;
import feign.Param;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WhatsMessageBatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WhatsMessageBatchRepository extends JpaRepository<WhatsMessageBatch, Long>, JpaSpecificationExecutor<WhatsMessageBatch> {
    Optional<WhatsMessageBatch> findByWaidToAndStatus(String waid, WhatsStatus status);

    List<WhatsMessageBatch> findByWaidTo(String waid);

    List<WhatsMessageBatch> findByPerfilIDAndStatus(Integer perfil, WhatsStatus status);

    List<WhatsMessageBatch> findByOfertaIdAndStatus(Long ofertaId, WhatsStatus status);

    @Query(
        value = "select * FROM whats_message_batch w WHERE w.status = ?1 AND (EXTRACT('epoch' FROM now() - w.created_date + interval '3 hours') / 60) > ?2",
        nativeQuery = true
    )
    List<WhatsMessageBatch> findWith30Minutes(String status, int minute);
}
