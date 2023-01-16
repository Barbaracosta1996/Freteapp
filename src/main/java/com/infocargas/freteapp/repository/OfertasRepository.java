package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ofertas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfertasRepository extends JpaRepository<Ofertas, Long>, JpaSpecificationExecutor<Ofertas> {
    List<Ofertas> findByPerfilUserId(Long id);

    List<Ofertas> findAllByStatusAndDataFechamentoLessThanEqual(StatusOferta statusOferta, ZonedDateTime data);

    List<Ofertas> findAllByStatus(StatusOferta statusOferta);
}
