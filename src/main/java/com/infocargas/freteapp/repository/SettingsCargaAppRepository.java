package com.infocargas.freteapp.repository;

import com.infocargas.freteapp.domain.SettingsCargaApp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SettingsCargaApp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SettingsCargaAppRepository extends JpaRepository<SettingsCargaApp, Long> {}
