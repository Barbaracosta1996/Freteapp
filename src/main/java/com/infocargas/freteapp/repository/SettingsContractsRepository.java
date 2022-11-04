package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.SettingsContracts;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SettingsContracts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SettingsContractsRepository extends JpaRepository<SettingsContracts, Long> {}
