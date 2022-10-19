package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.Frota;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Frota entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FrotaRepository extends JpaRepository<Frota, Long>, JpaSpecificationExecutor<Frota> {}
