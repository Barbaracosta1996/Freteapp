package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.PerfilAnexos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PerfilAnexos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerfilAnexosRepository extends JpaRepository<PerfilAnexos, Long>, JpaSpecificationExecutor<PerfilAnexos> {}
