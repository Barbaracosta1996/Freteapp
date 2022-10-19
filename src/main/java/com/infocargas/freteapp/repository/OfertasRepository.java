package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.Ofertas;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Ofertas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfertasRepository extends JpaRepository<Ofertas, Long>, JpaSpecificationExecutor<Ofertas> {
    List<Ofertas> findByPerfilUserId(Long id);
}
