package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.CategoriaVeiculo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategoriaVeiculo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaVeiculoRepository extends JpaRepository<CategoriaVeiculo, Long>, JpaSpecificationExecutor<CategoriaVeiculo> {}
